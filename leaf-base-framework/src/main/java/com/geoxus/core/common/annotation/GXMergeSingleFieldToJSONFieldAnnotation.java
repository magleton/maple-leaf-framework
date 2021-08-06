package com.geoxus.core.common.annotation;

import java.lang.annotation.*;

/**
 * 合并指定的属性到数据库配置的JSON字段
 * <p>
 * 该注解如果不使用DTO转换,则需要配合{@code @TableField(exists=false)}使用
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXMergeSingleFieldToJSONFieldAnnotation {
    String dbJSONFieldName() default "ext";

    String dbFieldName() default "";
}
