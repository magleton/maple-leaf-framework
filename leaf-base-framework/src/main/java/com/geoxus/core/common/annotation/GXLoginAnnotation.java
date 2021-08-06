package com.geoxus.core.common.annotation;

import java.lang.annotation.*;

/**
 * 登录效验
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXLoginAnnotation {
}
