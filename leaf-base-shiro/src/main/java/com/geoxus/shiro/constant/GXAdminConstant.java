package com.geoxus.shiro.constant;

import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.core.common.util.GXFrameworkCommonUtils;

public class GXAdminConstant {
    @GXFieldComment(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "id";

    @GXFieldComment(zhDesc = "表名")
    public static final String TABLE_NAME = "admin";

    @GXFieldComment(zhDesc = "数据表别名")
    public static final String TABLE_ALIAS_NAME = "admin";

    @GXFieldComment("模型在数据库中的表述[一般跟数据表的名字相同]")
    public static final String MODEL_IDENTIFICATION_VALUE = "admin";

    @GXFieldComment("数据源")
    public static final String DATASOURCE = "framework";

    @GXFieldComment("核心模型ID")
    public static final int CORE_MODEL_ID = GXFrameworkCommonUtils.getEnvironmentValue("app.com.geoxus.core-model-id.admin", Integer.class, 100);

    protected GXAdminConstant() {
    }
}
