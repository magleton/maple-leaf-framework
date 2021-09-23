package com.geoxus.mongodb.datasource.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.common.exception.GXBusinessException;
import com.geoxus.mongodb.datasource.properties.GXMongoDynamicDataSourceProperties;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * mongo DB的多数据源配置
 *
 * @author britton <britton@126.com>
 * @since 2021-09-23
 */
@Configuration
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
            SimpleMongoClientDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(MongoClients.create(uri), database);
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
            throw new GXBusinessException("只能有一个主要的bean , 请检查isPrimary是否设置了多个true值!");
        }
        if (primaryBeanCnt.get() == 0) {
            throw new GXBusinessException("必须有一个主要的bean, 请检查isPrimary是否设置true值!");
        }
    }
}
