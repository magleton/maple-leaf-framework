package com.geoxus.core.common.annotation;

import java.lang.annotation.*;

/**
 * 注解敏感信息类的注解
 *
 * @author britton <britton@126.com>
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXSensitiveDataAnnotation {
}