package cn.maple.mongodb.datasource.config;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.mongodb.datasource.properties.GXMongoDynamicDataSourceProperties;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * mongo DB的多数据源配置
 *
 * @author britton <britton@126.com>
 * @since 2021-09-23
 */
@Component
@Slf4j
public class GXMongoConfig implements ApplicationContextAware {
    @Resource
    private GXMongoDynamicDataSourceProperties mongoDynamicDataSourceProperties;

    private ApplicationContext applicationContext;

    @Bean(name = "mongoTemplate")
    public MongoTemplate primaryMongoTemplate() {
        dealMongoDynamicDataSourceProperties();
        String[] primaryMongoTemplateName = {""};
        mongoDynamicDataSourceProperties.getDatasource().forEach((k, dataSourceProperties) -> {
            String uri = dataSourceProperties.getUri();
            String database = dataSourceProperties.getDatabase();
            String username = dataSourceProperties.getUsername();
            String authenticationDatabase = dataSourceProperties.getAuthenticationDatabase();
            char[] password = dataSourceProperties.getPassword();
            MongoCredential credential = MongoCredential.createCredential(username, authenticationDatabase, password);
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder().credential(credential).applyConnectionString(connectionString).build();
            //SimpleMongoClientDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(MongoClients.create(mongodb://name:pass@localhost:27017/databaseName), database);
            SimpleMongoClientDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoClientSettings), database);
            MongoTemplate mongoTemplate = new MongoTemplate(factory);
            Boolean isPrimary = dataSourceProperties.getIsPrimary();
            String beanName = dataSourceProperties.getBeanName();
            if (CharSequenceUtil.isBlank(beanName)) {
                beanName = k + "MongoTemplate";
            }
            if (Boolean.TRUE.equals(isPrimary)) {
                primaryMongoTemplateName[0] = beanName;
            }
            ((AbstractApplicationContext) applicationContext).getBeanFactory().registerSingleton(beanName, mongoTemplate);
        });
        return (MongoTemplate) applicationContext.getBean(primaryMongoTemplateName[0]);
    }

    @Override
    @SuppressWarnings("all")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void dealMongoDynamicDataSourceProperties() {
        AtomicInteger primaryBeanCnt = new AtomicInteger();
        mongoDynamicDataSourceProperties.getDatasource().forEach((k, properties) -> {
            Boolean isPrimary = properties.getIsPrimary();
            if (Boolean.TRUE.equals(isPrimary)) {
                primaryBeanCnt.getAndIncrement();
            }
        });
        if (primaryBeanCnt.get() > 1) {
            throw new GXBusinessException("只能有一个主MongoTemplate , 请检查isPrimary是否设置了多个!");
        }
        if (primaryBeanCnt.get() == 0) {
            throw new GXBusinessException("必须有一个主MongoTemplate , 请检查isPrimary是否被设置!");
        }
    }
}
