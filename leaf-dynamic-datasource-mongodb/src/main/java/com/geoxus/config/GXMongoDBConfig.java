package com.geoxus.config;

import com.geoxus.properties.MongoSettingsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;

import javax.annotation.Resource;

@Configuration
public class GXMongoDBConfig {
    @Resource
    private MongoSettingsProperties mongoSettingsProperties;

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        return null;
    }
}