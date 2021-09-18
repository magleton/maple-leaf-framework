package com.geoxus.commons.constant;

import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.core.common.util.GXCommonUtils;

public class GXMediaLibraryConstant {
    @GXFieldComment(zhDesc = "主键ID")
    public static final String PRIMARY_KEY = "id";

    @GXFieldComment(zhDesc = "表名")
    public static final String TABLE_NAME = "media_library";

    @GXFieldComment(zhDesc = "数据表别名")
    public static final String TABLE_ALIAS_NAME = "media_library";

    @GXFieldComment("模型在数据库中的表述[一般跟数据表的名字相同]")
    public static final String MODEL_IDENTIFICATION_VALUE = "media_library";

    @GXFieldComment("数据源")
    public static final String DATASOURCE = "framework";

    @GXFieldComment("核心模型ID")
    public static final int CORE_MODEL_ID = GXCommonUtils.getEnvironmentValue("app.core-model-id.media_library", Integer.class, 100);

    @GXFieldComment("目标ID的字段名字")
    public static final String TARGET_ID_FIELD_NAME = "targetId";

    @GXFieldComment("资源类型的字段名字")
    public static final String RESOURCE_TYPE_FIELD_NAME = "resourceType";

    private GXMediaLibraryConstant() {

    }
}
