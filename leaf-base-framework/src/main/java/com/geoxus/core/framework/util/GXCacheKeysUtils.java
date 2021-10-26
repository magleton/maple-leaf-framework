package com.geoxus.core.framework.util;

import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.framework.factory.GXYamlPropertySourceFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class GXCacheKeysUtils {
    /**
     * 获取系统配置的KEY
     *
     * @param key 缓存KEY
     * @return String
     */
    public String getSysConfigKey(String key) {
        return getCacheKey("sys.config", key);
    }

    /**
     * 获取图形验证码的KEY
     *
     * @param key 缓存KEY
     * @return String
     */
    public String getCaptchaConfigKey(String key) {
        return getCacheKey("sys.captcha", key);
    }

    /**
     * 获取手机验证码的KEY(网易云)
     *
     * @param key 缓存KEY
     * @return String
     */
    public String getNetEaseSMSCodeConfigKey(String key) {
        return getCacheKey("sys.net-ease", key);
    }

    /**
     * 获取手机验证码的KEY(阿里云)
     *
     * @param key 缓存KEY
     * @return String
     */
    public String getAliYunSMSCodeConfigKey(String key) {
        return getCacheKey("sys.ali-yun", key);
    }

    /**
     * 根据配置文件的name和key获取Redis的key
     *
     * @param configName 配置名字
     * @param key        配置的key
     * @return String
     */
    public String getCacheKey(String configName, String key) {
        CacheKeysProperties cacheKeysProperties = GXSpringContextUtil.getBean(CacheKeysProperties.class);
        if (Objects.isNull(cacheKeysProperties)) {
            log.info("未配置缓存键列表");
            return CharSequenceUtil.format("geoxus:default:{}", key);
        }
        final List<Map<String, String>> list = cacheKeysProperties.getKeys();
        log.info(list.toString());
        for (Map<String, String> map : list) {
            final String s = map.get(configName);
            if (null != s && !s.isEmpty()) {
                return s + ":" + key;
            }
        }
        if (CharSequenceUtil.isNotBlank(key)) {
            return CharSequenceUtil.format("geoxus:default:{}", key);
        }
        return "geoxus:default:key";
    }

    @Data
    @Component
    @Configuration
    @ConditionalOnExpression("${cache.enable:0}==1")
    @PropertySource(value = {"classpath:/${spring.profiles.active}/cache-key.yml"},
            factory = GXYamlPropertySourceFactory.class,
            encoding = "UTF-8",
            ignoreResourceNotFound = true)
    @ConfigurationProperties(prefix = "cache-key")
    static class CacheKeysProperties {
        private List<Map<String, String>> keys;
    }
}
