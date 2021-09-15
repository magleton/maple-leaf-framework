package com.geoxus.core.common.annotation;

import com.geoxus.core.common.validator.GXValidateJsonFieldService;
import com.geoxus.core.common.validator.impl.GXValidateSingleJsonFieldValidator;
import com.geoxus.core.framework.service.impl.GXValidateJsonFieldServiceImpl;

import javax.validation.Constraint;
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
@Constraint(validatedBy = GXValidateSingleJsonFieldValidator.class)
public @interface GXMergeSingleFieldAnnotation {
    String message() default "{fieldName}数据验证出错";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends GXValidateJsonFieldService> service() default GXValidateJsonFieldServiceImpl.class;

    String tableName() default "";

    String parentFieldName() default "ext";

    String fieldName();
}
