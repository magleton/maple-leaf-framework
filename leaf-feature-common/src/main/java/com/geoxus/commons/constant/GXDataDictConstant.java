package com.geoxus.commons.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.util.GXCommonUtils;

public class GXDataDictConstant {
    @GXFieldCommentAnnotation(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "id";

    @GXFieldCommentAnnotation(zhDesc = "表名")
    public static final String TABLE_NAME = "data_dict";

    @GXFieldCommentAnnotation(zhDesc = "数据表别名")
    public static final String TABLE_ALIAS_NAME = "data_dict";

    @GXFieldCommentAnnotation("模型在数据库中的表述[一般跟数据表的名字相同]")
    public static final String MODEL_IDENTIFICATION_VALUE = "data_dict";

    @GXFieldCommentAnnotation("数据源")
    public static final String DATASOURCE = "framework";

    @GXFieldCommentAnnotation("核心模型ID")
    public static final int CORE_MODEL_ID = GXCommonUtils.getEnvironmentValue("app.core-model-id.data_dict", Integer.class);

    private GXDataDictConstant() {

    }
}
