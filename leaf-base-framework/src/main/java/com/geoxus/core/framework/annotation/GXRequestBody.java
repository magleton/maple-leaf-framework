package com.geoxus.core.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface GXRequestBody {
    String value() default "";

    Class<?>[] groups() default {};

    boolean fillJSONField() default true;

    boolean validateTarget() default true;

    boolean validateCoreModelId() default true;

    String primaryKey() default "id";
}
