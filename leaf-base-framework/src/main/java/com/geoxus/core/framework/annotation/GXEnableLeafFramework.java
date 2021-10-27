package com.geoxus.core.framework.annotation;

import com.geoxus.core.framework.config.GXFrameworkConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({GXFrameworkConfig.class})
public @interface GXEnableLeafFramework {
}
