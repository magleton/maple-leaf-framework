package com.geoxus.mongodb.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GXMongoDataSourceAnnotation {
    String value() default "";
}
