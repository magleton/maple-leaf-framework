package com.geoxus.commons.annotation;

import com.geoxus.core.common.service.GXApiIdempotentService;

import java.lang.annotation.*;

/**
 * API幂等注解
 * 可以防止同一表单提交多次的情况
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXApiIdempotentAnnotation {
    /**
     * Token过期时间
     */
    int expires() default 60;

    /**
     * 自定义service
     */
    Class<? extends GXApiIdempotentService> service() default GXApiIdempotentService.class;

    /**
     * 自定义方法
     */
    String methodName() default "customApiIdempotentValidate";
}
