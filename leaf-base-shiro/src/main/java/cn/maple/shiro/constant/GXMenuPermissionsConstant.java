package cn.maple.shiro.constant;

import cn.maple.core.framework.annotation.GXFieldComment;

public class GXMenuPermissionsConstant {
    @GXFieldComment(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "id";

    @GXFieldComment(zhDesc = "表名")
    public static final String TABLE_NAME = "s_menu_permissions";

    @GXFieldComment(zhDesc = "数据表别名")
    public static final String TABLE_ALIAS_NAME = "menu_permissions";

    @GXFieldComment("模型在数据库中的表述[一般跟数据表的名字相同]")
    public static final String MODEL_IDENTIFICATION_VALUE = "menu_permissions";

    @GXFieldComment("数据源")
    public static final String DATASOURCE = "permissions";

    protected GXMenuPermissionsConstant() {
    }
}
