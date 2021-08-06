package com.geoxus.core.common.annotation;

import com.geoxus.core.common.validator.GXValidateExtDataService;
import com.geoxus.core.common.validator.impl.GXValidateExtDataValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Retention(RUNTIME)
@Constraint(validatedBy = GXValidateExtDataValidator.class)
@Documented
public @interface GXValidateExtDataAnnotation {
    String message() default "{fieldName}数据验证出错";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends GXValidateExtDataService> service() default GXValidateExtDataService.class;

    String tableName();

    String fieldName() default "ext";

    boolean isFullMatchAttribute() default false;
}
