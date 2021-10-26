package com.geoxus.feature.properties;

import com.geoxus.core.framework.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConditionalOnExpression("${upload.enable:0}==1")
@ConfigurationProperties(prefix = "upload")
@PropertySource(value = {"classpath:/${spring.profiles.active}/upload.yml"},
        factory = GXYamlPropertySourceFactory.class,
        ignoreResourceNotFound = true,
        encoding = "utf-8")
public class GXUploadProperties {
    private String depositPath;

    private String returnPath;
}
