package com.geoxus.shiro.constant;

import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.core.common.util.GXCommonUtils;

public class GXAdminRoleConstant {
    @GXFieldComment(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "id";

    @GXFieldComment(zhDesc = "表名")
    public static final String TABLE_NAME = "admin_role";

    @GXFieldComment(zhDesc = "数据表别名")
    public static final String TABLE_ALIAS_NAME = "admin_role";

    @GXFieldComment("模型在数据库中的表述[一般跟数据表的名字相同]")
    public static final String MODEL_IDENTIFICATION_VALUE = "admin_role";

    @GXFieldComment("数据源")
    public static final String DATASOURCE = "framework";

    @GXFieldComment("核心模型ID")
    public static final int CORE_MODEL_ID = GXCommonUtils.getEnvironmentValue("app.core-model-id.admin_role", Integer.class, 100);

    private GXAdminRoleConstant() {
    }
}
