package cn.maple.core.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated(since = "4.0.5", forRemoval = true)
public @interface GXRequestBody {
    String value() default "";

    Class<?>[] groups() default {};

    boolean validateTarget() default true;

    String idName() default "id";
}
