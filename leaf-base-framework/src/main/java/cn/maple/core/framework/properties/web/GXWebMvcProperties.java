package cn.maple.core.framework.properties.web;

import cn.maple.core.framework.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "web-mvc")
@PropertySource(value = {"classpath:/${spring.profiles.active}/web-mvc.yml"},
        factory = GXYamlPropertySourceFactory.class,
        encoding = "utf-8",
        ignoreResourceNotFound = true)
public class GXWebMvcProperties {
    private List<String> urlPatterns = new ArrayList<>();

    private List<String> resourcePatterns = new ArrayList<>();
}