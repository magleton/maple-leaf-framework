package cn.maple.core.framework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface GXMenu {
    /**
     * 菜单类型  1 : 目录  2 : 菜单  3 : 按钮
     */
    int menuType();

    /**
     * 菜单名字
     */
    String menuName();

    /**
     * 菜单码
     */
    String menuCode();

    /**
     * 菜单URL
     */
    String menuUrl();

    /**
     * 菜单图标
     */
    String menuIcon() default "";
}
