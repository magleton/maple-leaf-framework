package cn.maple.core.framework.annotation;

import java.lang.annotation.*;

/**
 * 业务日志注解
 * 可以作用在控制器或其他业务类上，用于描述当前类的功能；
 * 也可以用于方法上，用于描述当前方法的作用；
 *
 * @author 子曦
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GXBusinessLog {
    /**
     * 功能名称
     */
    String name() default "";

    /**
     * 功能描述
     */
    String description() default "";
}
