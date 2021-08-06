package com.geoxus.core.common.factory;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * 解析Yaml配置文件工厂类
 *
 * @author zj chen <britton@126.com>
 */
public class GXYamlPropertySourceFactory extends DefaultPropertySourceFactory {
    @Override
    @NonNull
    public PropertySource<?> createPropertySource(@Nullable String name, @NonNull EncodedResource resource) throws IOException {
        List<PropertySource<?>> propertySourceList = new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource());
        if (!propertySourceList.isEmpty()) {
            return propertySourceList.iterator().next();
        }
        return super.createPropertySource(name, resource);
    }
}