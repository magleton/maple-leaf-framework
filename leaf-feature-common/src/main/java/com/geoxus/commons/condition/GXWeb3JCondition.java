package com.geoxus.commons.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Configurable
@Slf4j
@SuppressWarnings("unused")
public class GXWeb3JCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        final String property = Optional.ofNullable(environment.getProperty("eth-enable", String.class)).orElse("");
        log.info("Web3JCondition : " + property);
        return "true".equals(property);
    }
}
