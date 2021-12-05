package cn.maple.core.framework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface GXPermissionCtl {
    /**
     * 权限所属模块的名字
     */
    String moduleName();

    /**
     * 权限所属模块的code
     */
    String moduleCode();
}
