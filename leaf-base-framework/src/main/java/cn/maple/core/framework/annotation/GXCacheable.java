package cn.maple.core.framework.annotation;

import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.dto.res.GXBaseResDto;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GXCacheable {
    /**
     * 如果命中缓存 将缓存的类型转换为该类型
     */
    Class<? extends GXBaseResDto> retType() default GXBaseResDto.class;

    /**
     * 转换到指定类型可以指定该值来进行自定义转换规则
     */
    String methodName() default GXCommonConstant.DEFAULT_CUSTOMER_PROCESS_METHOD_NAME;

    /**
     * 缓存key
     */
    String cacheKey() default "";
}
