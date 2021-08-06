package com.geoxus.core.datasource.factory;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;

public class GXDSYamlPropertySourceFactory extends DefaultPropertySourceFactory {
    @Override
    @NonNull
    public org.springframework.core.env.PropertySource<?> createPropertySource(@Nullable String name, @NonNull EncodedResource resource) throws IOException {
        List<org.springframework.core.env.PropertySource<?>> propertySourceList = new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource());
        if (!propertySourceList.isEmpty()) {
            return propertySourceList.iterator().next();
        }
        return super.createPropertySource(name, resource);
    }
}