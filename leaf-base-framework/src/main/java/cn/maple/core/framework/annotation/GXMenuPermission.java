package cn.maple.core.framework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface GXMenuPermission {
    /**
     * 权限名字
     */
    String permissionName();

    /**
     * 权限码
     */
    String permissionCode();

    /**
     * 菜单所属分组  eg : 查看需要列表和详情 , 则列表和详情应该归为一组
     */
    String menuGroup();

    /**
     * 菜单URL
     */
    String menuUrl();

    /**
     * 菜单名字
     */
    String menuName();

    /**
     * 菜单类型  1 : 目录  2 : 菜单  3 : 按钮
     */
    int menuType();

    /**
     * 菜单图标
     */
    String menuIcon() default "";
}
