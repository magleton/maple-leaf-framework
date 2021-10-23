package com.geoxus.core.framework.constant;

import com.geoxus.common.annotation.GXFieldComment;

public class GXCoreAttributeEnumsConstant {
    @GXFieldComment(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "attribute_enum_id";

    @GXFieldComment(zhDesc = "数据表名")
    public static final String TABLE_NAME = "core_attributes_enums";

    @GXFieldComment(zhDesc = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "core_attributes_enums";

    @GXFieldComment(zhDesc = "模型的值")
    public static final String MODEL_IDENTIFICATION_VALUE = "core_attributes_enums";

    private GXCoreAttributeEnumsConstant() {
    }
}
