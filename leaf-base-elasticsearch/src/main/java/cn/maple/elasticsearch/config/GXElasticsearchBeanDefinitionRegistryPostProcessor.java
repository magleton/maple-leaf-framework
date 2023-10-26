package cn.maple.elasticsearch.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.config.aware.GXApplicationContextSingleton;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.elasticsearch.properties.GXElasticsearchProperties;
import cn.maple.elasticsearch.properties.GXElasticsearchSourceProperties;
import cn.maple.elasticsearch.properties.local.GXLocalElasticsearchProperties;
import cn.maple.elasticsearch.properties.nacos.GXNacosElasticsearchProperties;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.http.HttpHeaders;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * mongo DB的多数据源配置
 *
 * @author britton <britton@126.com>
 * @since 2023-08-24
 */
@Configuration
@Log4j2
public class GXElasticsearchBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware, PriorityOrdered {
    private Environment environment;

    private ApplicationContext applicationContext;

    /**
     * Modify the application context's internal bean definition registry after its
     * standard initialization. All regular bean definitions will have been loaded,
     * but no beans will have been instantiated yet. This allows for adding further
     * bean definitions before the next post-processing phase kicks in.
     *
     * @param beanDefinitionRegistry the bean definition registry used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        getSourceElasticsearchProperties().getDatasource().forEach((key, dataSourceProperties) -> {
            ElasticsearchClient elasticsearchClient = buildElasticsearchClient(dataSourceProperties);
            // 创建ElasticsearchTemplate的BeanDefinition构建对象
            BeanDefinitionBuilder elasticsearchTemplateBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ElasticsearchTemplate.class);
            elasticsearchTemplateBeanDefinitionBuilder.addConstructorArgValue(elasticsearchClient);
            MappingElasticsearchConverter mappingElasticsearchConverter = new MappingElasticsearchConverter(new SimpleElasticsearchMappingContext());
            mappingElasticsearchConverter.afterPropertiesSet();
            elasticsearchTemplateBeanDefinitionBuilder.addConstructorArgValue(mappingElasticsearchConverter);
            elasticsearchTemplateBeanDefinitionBuilder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
            boolean primary = dataSourceProperties.isPrimary();
            String beanName = key + "ElasticsearchTemplate";
            if (Boolean.TRUE.equals(primary)) {
                settingElasticsearchPropertiesBeanProperties(dataSourceProperties);
                elasticsearchTemplateBeanDefinitionBuilder.setPrimary(true);
            }
            beanDefinitionRegistry.registerBeanDefinition(beanName, elasticsearchTemplateBeanDefinitionBuilder.getBeanDefinition());
        });
    }

    /**
     * 重新将IOC中的ElasticsearchProperties的bean对象填充属性
     *
     * @param dataSourceProperties 自定义配置的属性
     */
    private void settingElasticsearchPropertiesBeanProperties(GXElasticsearchProperties dataSourceProperties) {
        ElasticsearchProperties elasticsearchProperties = GXSpringContextUtils.getBean(ElasticsearchProperties.class);
        assert elasticsearchProperties != null;
        elasticsearchProperties.setUsername(dataSourceProperties.getUsername());
        elasticsearchProperties.setPassword(dataSourceProperties.getPassword());
        elasticsearchProperties.setUris(stringToLst(dataSourceProperties.getUris().get(0)));
        if (CharSequenceUtil.isNotEmpty(dataSourceProperties.getPathPrefix())) {
            elasticsearchProperties.setPathPrefix(dataSourceProperties.getPathPrefix());
        }
        Duration socketTimeout = dataSourceProperties.getSocketTimeout();
        Duration connectionTimeout = dataSourceProperties.getConnectionTimeout();
        elasticsearchProperties.setConnectionTimeout(connectionTimeout);
        elasticsearchProperties.setSocketTimeout(socketTimeout);
    }

    /**
     * Modify the application context's internal bean factory after its standard
     * initialization. All bean definitions will have been loaded, but no beans
     * will have been instantiated yet. This allows for overriding or adding
     * properties even to eager-initializing beans.
     *
     * @param beanFactory the bean factory used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(GXApplicationContextSingleton.INSTANCE.getApplicationContext())) {
            GXApplicationContextSingleton.INSTANCE.setApplicationContext(applicationContext);
        }
        this.applicationContext = GXApplicationContextSingleton.INSTANCE.getApplicationContext();
    }

    /**
     * Set the {@code Environment} that this component runs in.
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 解析配置文件中的连接信息
     *
     * @return GXElasticsearchSourceProperties 数据源配置信息
     */
    private GXElasticsearchSourceProperties getSourceElasticsearchProperties() {
        BindResult<Dict> bind = Binder.get(this.environment).bind("elasticsearch.datasource", Dict.class);
        Map<String, GXElasticsearchProperties> datasource = Convert.convert(new TypeReference<>() {
        }, bind.get());
        try {
            // 判断是否导入了nacos 导入了nacos 则使用nacos的配置
            Class.forName("com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties");
            GXNacosElasticsearchProperties elasticsearchSourceProperties = new GXNacosElasticsearchProperties();
            elasticsearchSourceProperties.setDatasource(datasource);
            return elasticsearchSourceProperties;
        } catch (ClassNotFoundException e) {
            // 不存在nacos 则使用local配置
        }
        GXLocalElasticsearchProperties elasticsearchSourceProperties = new GXLocalElasticsearchProperties();
        elasticsearchSourceProperties.setDatasource(datasource);
        return elasticsearchSourceProperties;
    }

    /**
     * 通过配置文件构建ElasticsearchTemplate对象
     *
     * @param elasticsearchSourceProperties 配置信息
     */
    private ElasticsearchClient buildElasticsearchClient(GXElasticsearchProperties elasticsearchSourceProperties) {
        ClientConfiguration.MaybeSecureClientConfigurationBuilder configurationBuilder = buildElasticsearchConfigurationBuilder(elasticsearchSourceProperties);
        return ElasticsearchClients.createImperative(configurationBuilder.build());
    }

    /**
     * 通过配置文件构建客户端配置 ClientConfiguration
     *
     * @param elasticsearchSourceProperties 配置信息
     */
    private ClientConfiguration.MaybeSecureClientConfigurationBuilder buildElasticsearchConfigurationBuilder(GXElasticsearchProperties elasticsearchSourceProperties) {
        String username = elasticsearchSourceProperties.getUsername();
        String password = elasticsearchSourceProperties.getPassword();
        String uriStr = elasticsearchSourceProperties.getUris().get(0);
        String[] uris = stringToLst(uriStr).toArray(new String[0]);
        ClientConfiguration.MaybeSecureClientConfigurationBuilder configurationBuilder = ClientConfiguration.builder().connectedTo(uris);
        if (CharSequenceUtil.isAllNotEmpty(username, password)) {
            configurationBuilder.withBasicAuth(username, password);
        }
        if (CharSequenceUtil.isNotEmpty(elasticsearchSourceProperties.getPathPrefix())) {
            configurationBuilder.withPathPrefix(elasticsearchSourceProperties.getPathPrefix());
        }
        HttpHeaders compatibilityHeaders = new HttpHeaders();
        compatibilityHeaders.add("Accept", "application/vnd.elasticsearch+json;compatible-with=7");
        //compatibilityHeaders.add(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7");
        compatibilityHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        configurationBuilder.withDefaultHeaders(compatibilityHeaders).withHeaders(() -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add("currentDate", DateUtil.now());
            return headers;
        }).withClientConfigurer(ElasticsearchClients.ElasticsearchClientConfigurationCallback.from(clientBuilder -> {
            clientBuilder.disableAuthCaching();
            //clientBuilder.setDefaultHeaders(List.of(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json")));
            clientBuilder.addInterceptorLast((HttpResponseInterceptor) (response, context) -> response.addHeader("X-Elastic-Product", "Elasticsearch"));
            return clientBuilder/*.setDefaultCredentialsProvider(credentialsProvider)*/;
        }));
        Duration connectionTimeout = elasticsearchSourceProperties.getConnectionTimeout();
        Duration socketTimeout = elasticsearchSourceProperties.getSocketTimeout();
        configurationBuilder.withConnectTimeout(connectionTimeout).withSocketTimeout(socketTimeout);
        return configurationBuilder;
    }

    /**
     * 将字符串转换为字符串数组
     *
     * @param uriStr 待转换的字符串 eg "{0=192.168.7.213:9200, 1=192.168.7.213:9200}"
     */
    private List<String> stringToLst(String uriStr) {
        List<String> lstUris = new ArrayList<>();
        Objects.requireNonNull(GXCommonUtils.convertStrToTarget(uriStr, Dict.class)).forEach((k, value) -> lstUris.add(value.toString()));
        return lstUris;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;  // within PriorityOrdered
    }
}
