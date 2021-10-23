package com.geoxus.shiro.constant;

import com.geoxus.core.framework.annotation.GXFieldComment;

public class GXAdminConstant {
    @GXFieldComment(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "id";

    @GXFieldComment(zhDesc = "表名")
    public static final String TABLE_NAME = "s_admin";

    @GXFieldComment(zhDesc = "数据表别名")
    public static final String TABLE_ALIAS_NAME = "admin";

    @GXFieldComment("模型在数据库中的表述[一般跟数据表的名字相同]")
    public static final String MODEL_IDENTIFICATION_VALUE = "admin";

    @GXFieldComment("数据源")
    public static final String DATASOURCE = "permissions";
    
    protected GXAdminConstant() {
    }
}
