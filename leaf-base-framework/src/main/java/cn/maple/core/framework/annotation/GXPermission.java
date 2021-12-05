package cn.maple.core.framework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface GXPermission {
    /**
     * 权限名字
     */
    String permissionName();

    /**
     * 权限码
     */
    String permissionCode();

    /**
     * 权限所属模块
     */
    String moduleCode() default "";

    /**
     * 权限所属模块名字
     */
    String moduleName() default "";
}
