package com.geoxus.properties;

import com.geoxus.core.datasource.factory.GXDSYamlPropertySourceFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@SuppressWarnings("all")
@Component
@PropertySource(value = {"classpath:/ymls/${spring.profiles.active}/mongodb.yml"},
        factory = GXDSYamlPropertySourceFactory.class,
        ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "mongodb")
@ConditionalOnMissingClass({"com.alibaba.nacos.api.config.ConfigFactory"})
public class MongoSettingsProperties {
    private Integer minConnectionsPerHost;

    private Integer connectionsPerHost;

    private String database;

    private List<String> hosts;

    private List<Integer> ports;

    private String replicaSet;

    private String username;

    private String password;

    private String authenticationDatabase;
}