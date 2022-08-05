package cn.maple.core.datasource.annotation;

import java.lang.annotation.*;

/**
 * 标记注解
 * <p>
 * 添加了该注解的model类
 * 在MyBatis-Plus自动填充时
 * 不会调用自定的获取用户的类的实列
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXAppStartFillData {
    /**
     * 创建者的名字
     */
    String value() default "system";
}
