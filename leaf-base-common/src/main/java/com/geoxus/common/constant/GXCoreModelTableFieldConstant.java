package com.geoxus.common.constant;

import com.geoxus.core.framework.annotation.GXFieldComment;

public class GXCoreModelTableFieldConstant {
    @GXFieldComment(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "core_model_table_field_id";

    @GXFieldComment(zhDesc = "数据表名")
    public static final String TABLE_NAME = "core_model_table_field";

    @GXFieldComment(zhDesc = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "core_model_table_field";

    @GXFieldComment(zhDesc = "模型的值")
    public static final String MODEL_IDENTIFICATION_VALUE = "core_model_table_field";

    private GXCoreModelTableFieldConstant() {
    }
}
