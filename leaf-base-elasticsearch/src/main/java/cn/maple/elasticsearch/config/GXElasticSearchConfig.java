package cn.maple.elasticsearch.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.elasticsearch.properties.GXElasticsearchProperties;
import cn.maple.elasticsearch.properties.GXElasticsearchSourceProperties;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.http.HttpHeaders;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Map;

/*@Configuration
@Log4j2*/
public class GXElasticSearchConfig extends ElasticsearchConfiguration {
    @Resource
    private GXElasticsearchSourceProperties elasticsearchSourceProperties;

    @Override
    public ClientConfiguration clientConfiguration() {
        Map<String, GXElasticsearchProperties> datasource = elasticsearchSourceProperties.getDatasource();
        GXElasticsearchProperties elasticsearchProperties = datasource.get("framework");
        String username = elasticsearchProperties.getUsername();
        String password = elasticsearchProperties.getPassword();
        ClientConfiguration.MaybeSecureClientConfigurationBuilder configurationBuilder = ClientConfiguration.builder()
                .connectedTo("");
        if (CharSequenceUtil.isAllNotEmpty(username, password)) {
            configurationBuilder.withBasicAuth(username, password);
        }
        HttpHeaders compatibilityHeaders = new HttpHeaders();
        compatibilityHeaders.add("Accept", "application/vnd.elasticsearch+json;compatible-with=7");
        //compatibilityHeaders.add(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7");
        compatibilityHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("", ""));
        configurationBuilder
                .withDefaultHeaders(compatibilityHeaders)
                .withHeaders(() -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("currentDate", DateUtil.now());
                    return headers;
                })
                .withClientConfigurer(
                        ElasticsearchClients.ElasticsearchClientConfigurationCallback.from(clientBuilder -> {
                            clientBuilder.disableAuthCaching();
                            //clientBuilder.setDefaultHeaders(List.of(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json")));
                            clientBuilder.addInterceptorLast((HttpResponseInterceptor) (response, context) -> response.addHeader("X-Elastic-Product", "Elasticsearch"));
                            return clientBuilder/*.setDefaultCredentialsProvider(credentialsProvider)*/;
                        }));
        configurationBuilder
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(3));
        return configurationBuilder.build();
    }
}