package com.geoxus.core.framework.config;

import com.geoxus.core.framework.validator.impl.GXValidateDBUniqueValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GXFrameWorkConfig {
    @Bean
    public GXValidateDBUniqueValidator validateDBUniqueOrExistsValidator() {
        return new GXValidateDBUniqueValidator();
    }
}
