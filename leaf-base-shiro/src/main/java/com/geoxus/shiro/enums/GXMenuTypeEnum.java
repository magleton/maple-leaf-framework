package com.geoxus.shiro.enums;

public enum GXMenuTypeEnum {
    CATALOG(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮");
    
    /**
     * menu的类型码
     * 1、目录
     * 2、菜单
     * 3、按钮
     */
    private final Integer code;

    /**
     * menu类型的描述
     */
    private final String desc;

    GXMenuTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
