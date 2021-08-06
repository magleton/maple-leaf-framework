package com.geoxus.core.common.config;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.geoxus.core.common.factory.GXYamlPropertySourceFactory;
import com.geoxus.core.common.validator.impl.GXValidateDBUniqueValidator;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 通用配置类
 */
@Configuration
@PropertySource(value = "classpath:/ymls/${spring.profiles.active}/common.yml",
        factory = GXYamlPropertySourceFactory.class,
        ignoreResourceNotFound = true)
public class GXCoreCommonConfig {
    private static final Logger LOG = LoggerFactory.getLogger(GXCoreCommonConfig.class);

    @Bean
    public GXValidateDBUniqueValidator validateDBUniqueOrExistsValidator() {
        return new GXValidateDBUniqueValidator();
    }

    @Bean
    @ConditionalOnExpression("'${enable-fileupload-progress}'.equals('false')")
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10L));
        factory.setMaxRequestSize(DataSize.ofMegabytes(20L));
        return factory.createMultipartConfig();
    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        final ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        final SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        serializerProvider.setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                String fieldName = jsonGenerator.getOutputContext().getCurrentName();
                try {
                    // 反射获取字段类型
                    Field field = jsonGenerator.getCurrentValue().getClass().getDeclaredField(fieldName);
                    if (CharSequence.class.isAssignableFrom(field.getType())) {
                        // 字符串型空值""
                        jsonGenerator.writeString("");
                        return;
                    } else if (Collection.class.isAssignableFrom(field.getType())) {
                        // 列表型空值返回[]
                        // jsonGenerator.writeStartArray();
                        // jsonGenerator.writeEndArray();
                        // 返回一个null值
                        jsonGenerator.writeNull();
                        return;
                    } else if (Map.class.isAssignableFrom(field.getType())) {
                        // map型空值或者bean对象返回"{}"
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeEndObject();
                        return;
                    }
                } catch (NoSuchFieldException noSuchFieldException) {
                    LOG.info(noSuchFieldException.getMessage());
                }
                jsonGenerator.writeString("");
            }
        });
        return objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    /**
     * 配置缓存管理器
     *
     * @return 缓存管理器
     */
    @Bean("caffeineCache")
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCacheNames(CollUtil.newArrayList("FRAMEWORK-CACHE", "__DEFAULT__"));
        caffeineCacheManager.setAllowNullValues(false);
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterAccess(365, TimeUnit.DAYS)
                .initialCapacity(100)
                .maximumSize(100000));
        return caffeineCacheManager;
    }
}
