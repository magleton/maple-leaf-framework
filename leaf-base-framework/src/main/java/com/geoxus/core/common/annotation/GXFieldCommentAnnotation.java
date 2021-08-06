package com.geoxus.core.common.annotation;

import java.lang.annotation.*;

/**
 * 用于反射读取接口/类中定义的属性信息
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXFieldCommentAnnotation {
    String value() default "";

    String zhDesc() default "中文描述";

    String enDesc() default "英文描述";

    long code() default 0;

    boolean isShow() default false;
}
