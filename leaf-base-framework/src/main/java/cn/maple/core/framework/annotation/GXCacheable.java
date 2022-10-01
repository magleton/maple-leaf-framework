package cn.maple.core.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GXCacheable {
    /**
     * 缓存key
     */
    String cacheKey() default "";

    /**
     * 清除缓存数据
     */
    boolean evictCache() default false;
}
