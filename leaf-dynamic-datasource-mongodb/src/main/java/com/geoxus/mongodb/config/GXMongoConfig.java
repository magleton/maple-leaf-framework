package com.geoxus.mongodb.config;

import com.geoxus.mongodb.factory.GXSimpleMongoClientDatabaseFactory;
import com.geoxus.mongodb.properties.GXMongoDynamicDataSourceProperties;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAspectJAutoProxy
public class GXMongoConfig {
    private static final Map<String, GXSimpleMongoClientDatabaseFactory> targetFactory = new HashMap<>();

    @Resource
    private GXMongoDynamicDataSourceProperties mongoDynamicDataSourceProperties;

    @Bean
    public MongoTemplate mongoTemplate() {
        mongoDynamicDataSourceProperties.getDatasource().forEach((k, mongoDataSourceProperties) -> {
            String uri = mongoDataSourceProperties.getUri();
            String database = mongoDataSourceProperties.getDatabase();
            MongoClient mongoClient = MongoClients.create(uri);
            GXSimpleMongoClientDatabaseFactory factory = new GXSimpleMongoClientDatabaseFactory(mongoClient, database);
            targetFactory.put(k, factory);
        });
        return new MongoTemplate(targetFactory.get("framework"));
    }
}