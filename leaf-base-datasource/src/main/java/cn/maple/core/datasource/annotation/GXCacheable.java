package cn.maple.core.datasource.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GXCacheable {
    /**
     * 场景值
     */
    String scene() default "";
}
