package com.geoxus.core.framework.constant;

import com.geoxus.common.annotation.GXFieldCommentAnnotation;

public class GXCoreAttributeEnumsConstant {
    @GXFieldCommentAnnotation(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "attribute_enum_id";

    @GXFieldCommentAnnotation(zhDesc = "数据表名")
    public static final String TABLE_NAME = "core_attributes_enums";

    @GXFieldCommentAnnotation(zhDesc = "数据表的别名")
    public static final String TABLE_ALIAS_NAME = "core_attributes_enums";

    @GXFieldCommentAnnotation(zhDesc = "模型的值")
    public static final String MODEL_IDENTIFICATION_VALUE = "core_attributes_enums";

    private GXCoreAttributeEnumsConstant() {
    }
}
