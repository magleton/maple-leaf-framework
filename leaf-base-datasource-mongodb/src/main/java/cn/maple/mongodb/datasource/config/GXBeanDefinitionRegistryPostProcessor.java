package cn.maple.mongodb.datasource.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.config.aware.GXApplicationContextSingleton;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.mongodb.datasource.properties.GXMongoDataSourceProperties;
import cn.maple.mongodb.datasource.properties.GXMongoDynamicDataSourceProperties;
import cn.maple.mongodb.datasource.properties.local.GXLocalMongoDynamicDataSourceProperties;
import cn.maple.mongodb.datasource.properties.nacos.GXNacosMongoDynamicDataSourceProperties;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.*;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * mongo DB的多数据源配置
 *
 * @author britton <britton@126.com>
 * @since 2023-08-24
 */
@Component
@Log4j2
public class GXBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {
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
        checkMongoDynamicDataSourceProperties();
        getMongoDynamicDataSourceProperties().getDatasource().forEach((key, dataSourceProperties) -> {
            String uri = GXCommonUtils.decodeConnectStr(dataSourceProperties.getUri(), String.class);
            String database = dataSourceProperties.getDatabase();
            String username = Objects.isNull(dataSourceProperties.getUsername()) ? null : GXCommonUtils.decodeConnectStr(dataSourceProperties.getUsername(), String.class);
            String authenticationDatabase = GXCommonUtils.decodeConnectStr(dataSourceProperties.getAuthenticationDatabase(), String.class);
            char[] password = Objects.isNull(dataSourceProperties.getPassword()) ? null : GXCommonUtils.decodeConnectStr(String.valueOf(dataSourceProperties.getPassword()), String.class).toCharArray();
            MongoCredential credential = MongoCredential.createCredential(username, authenticationDatabase, password);
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder().credential(credential).applyConnectionString(connectionString).build();
            // 创建MongoDatabaseFactory的BeanDefinition构建对象
            BeanDefinitionBuilder mongoDatabaseFactoryBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(SimpleMongoClientDatabaseFactory.class);
            mongoDatabaseFactoryBeanDefinitionBuilder.addConstructorArgValue(MongoClients.create(mongoClientSettings));
            mongoDatabaseFactoryBeanDefinitionBuilder.addConstructorArgValue(database);
            mongoDatabaseFactoryBeanDefinitionBuilder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
            Boolean primary = dataSourceProperties.getPrimary();
            if (Boolean.TRUE.equals(primary)) {
                mongoDatabaseFactoryBeanDefinitionBuilder.setPrimary(true);
            }
            String factoryBeanName = key + "MongoDatabaseFactory";
            beanDefinitionRegistry.registerBeanDefinition(factoryBeanName, mongoDatabaseFactoryBeanDefinitionBuilder.getBeanDefinition());

            // 创建MongoTemplate的BeanDefinition构建对象
            BeanDefinitionBuilder mongoTemplateBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(MongoTemplate.class);
            SimpleMongoClientDatabaseFactory factory = applicationContext.getBean(factoryBeanName, SimpleMongoClientDatabaseFactory.class);
            mongoTemplateBeanDefinitionBuilder.addConstructorArgValue(factory);
            mongoTemplateBeanDefinitionBuilder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
            String beanName = dataSourceProperties.getBeanName();
            if (CharSequenceUtil.isEmpty(beanName)) {
                beanName = key + "MongoTemplate";
            }
            if (Boolean.TRUE.equals(primary)) {
                mongoTemplateBeanDefinitionBuilder.setPrimary(true);
                beanDefinitionRegistry.registerAlias(beanName, "mongoTemplate");
            }
            beanDefinitionRegistry.registerBeanDefinition(beanName, mongoTemplateBeanDefinitionBuilder.getBeanDefinition());
        });
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
     * 检测配置中是否只有一个配置项
     * 配置了primary为true
     */
    private void checkMongoDynamicDataSourceProperties() {
        AtomicInteger primaryBeanCnt = new AtomicInteger();
        getMongoDynamicDataSourceProperties().getDatasource().forEach((k, properties) -> {
            Boolean primary = properties.getPrimary();
            if (Boolean.TRUE.equals(primary)) {
                primaryBeanCnt.getAndIncrement();
            }
        });
        if (primaryBeanCnt.get() > 1) {
            throw new GXBusinessException("只能有一个主MongoTemplate , 请检查primary是否设置了多个!");
        }
        if (primaryBeanCnt.get() == 0) {
            throw new GXBusinessException("必须有一个主MongoTemplate , 请检查primary是否被设置!");
        }
    }

    /**
     * 解析配置文件中的连接信息
     *
     * @return GXMongoDynamicDataSourceProperties 数据源配置信息
     */
    private GXMongoDynamicDataSourceProperties getMongoDynamicDataSourceProperties() {
        BindResult<Dict> bind = Binder.get(this.environment).bind("mongodb.datasource", Dict.class);
        Map<String, GXMongoDataSourceProperties> datasource = Convert.convert(new TypeReference<>() {
        }, bind.get());
        try {
            // 判断是否导入了nacos 导入了nacos 则使用nacos的配置
            Class.forName("com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties");
            GXNacosMongoDynamicDataSourceProperties mongoDynamicDataSourceProperties = new GXNacosMongoDynamicDataSourceProperties();
            mongoDynamicDataSourceProperties.setDatasource(datasource);
            return mongoDynamicDataSourceProperties;
        } catch (ClassNotFoundException e) {
            // 不存在nacos 则使用local配置
        }
        GXLocalMongoDynamicDataSourceProperties mongoDynamicDataSourceProperties = new GXLocalMongoDynamicDataSourceProperties();
        mongoDynamicDataSourceProperties.setDatasource(datasource);
        return mongoDynamicDataSourceProperties;
    }
}
