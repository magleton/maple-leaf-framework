package com.geoxus.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface GXRequestBodyToTargetAnnotation {
    String value() default "";

    Class<?>[] groups() default {};

    boolean fillJSONField() default true;

    boolean validateTarget() default true;

    boolean validateCoreModelId() default true;

    String primaryKey() default "id";

    Class<?> mapstructClazz() default Void.class;

    boolean isConvertToEntity() default false;
}
