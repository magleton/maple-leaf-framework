package cn.maple.sso.annotation;

import cn.maple.sso.enums.GXAction;

import java.lang.annotation.*;

/**
 * <p>
 * sso 权限注解
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GXPermissionAnnotation {
    /**
     * 权限内容
     */
    String value() default "";

    /**
     * 执行动作
     * {@link GXAction}
     */
    GXAction action() default GXAction.Normal;
}