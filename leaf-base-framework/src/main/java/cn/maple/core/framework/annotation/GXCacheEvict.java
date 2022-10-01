package cn.maple.core.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GXCacheEvict {
    /**
     * 缓存key
     */
    String cacheKey() default "";
}
