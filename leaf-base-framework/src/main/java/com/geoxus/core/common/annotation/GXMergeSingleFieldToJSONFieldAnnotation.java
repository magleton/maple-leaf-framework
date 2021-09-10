package com.geoxus.core.common.annotation;

import com.geoxus.core.common.validator.GXValidateJSONFieldService;

import javax.validation.Payload;
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
    String message() default "{fieldName}数据验证出错";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends GXValidateJSONFieldService> service() default GXValidateJSONFieldService.class;

    String tableName() default "";

    String dbJSONFieldName() default "ext";

    String dbFieldName() default "";
}
