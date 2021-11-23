package cn.maple.core.framework.properties.web;

import cn.maple.core.framework.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * beanName的配置需要注意以下规则:
 * 当类的名字是以两个或以上的大写字母开头的话，bean的名字会与类名保持一致
 *
 * @author britton
 */
@Data
@Component
@ConfigurationProperties(prefix = "web-mvc")
@PropertySource(value = {"classpath:/${spring.profiles.active}/web-mvc.yml"},
        factory = GXYamlPropertySourceFactory.class,
        encoding = "utf-8",
        ignoreResourceNotFound = true)
public class GXWebMvcProperties {
    private Map<String, WebMvcItemProperties> platform = new LinkedHashMap<>();

    @Data
    public static class WebMvcItemProperties {
        private String beanName;

        private List<String> urlPatterns;

        private List<String> resourcePatterns;
    }
}