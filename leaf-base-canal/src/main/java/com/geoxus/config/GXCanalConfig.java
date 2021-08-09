package com.geoxus.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Configuration
@EnableConfigurationProperties
public class GXCanalConfig {
    @Autowired
    private CanalConfig canalConfig;

    @Bean
    public CanalConnector canalConnector() {
        return CanalConnectors.newSingleConnector(
                new InetSocketAddress(
                        canalConfig.getHost(),
                        canalConfig.getPort()),
                canalConfig.getDestination(),
                canalConfig.getUsername(),
                canalConfig.getPassword());
    }

    @Component("canalConfig")
    @PropertySource(value = "classpath:/ymls/${spring.profiles.active}/canal.yml",
            factory = GXYamlPropertySourceFactory.class,
            ignoreResourceNotFound = true)
    @ConfigurationProperties(prefix = "canal")
    @Data
    protected static class CanalConfig {
        private String host;

        private Integer port;

        private String destination;

        private String username;

        private String password;
    }
}
