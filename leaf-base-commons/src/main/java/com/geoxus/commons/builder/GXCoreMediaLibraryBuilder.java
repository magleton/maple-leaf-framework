package com.geoxus.commons.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXCommonConstants;
import org.apache.ibatis.jdbc.SQL;

@SuppressWarnings("unused")
public class GXCoreMediaLibraryBuilder implements GXBaseBuilder {
    private static final String DB_FIELDS = "id, core_model_id, model_type, object_id, collection_name, name, file_name, size, manipulations, custom_properties, responsive_images, order_column, resource_type";

    /**
     * 字段名字
     */
    private static final String TARGET_ID_FIELD_NAME = "target_id";

    /**
     * 数据表名
     */
    private static final String TABLE_NAME = "media_library";

    public String listOrSearch(Dict param) {
        Long targetId = param.getLong(TARGET_ID_FIELD_NAME);
        Long coreModelId = param.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME);
        return new SQL()
                .SELECT("*").FROM(TABLE_NAME)
                .WHERE(CharSequenceUtil.format("target_id={} and core_model_id={}", targetId, coreModelId))
                .toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return TABLE_NAME;
    }

    public String deleteByCondition(Dict param) {
        Long targetId = param.getLong(TARGET_ID_FIELD_NAME);
        Long coreModelId = param.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME);
        final SQL sql = new SQL()
                .DELETE_FROM(TABLE_NAME)
                .WHERE(CharSequenceUtil.format("target_id = {} and core_model_id = {}", targetId, coreModelId));
        return sql.toString();
    }

    public String detail(Dict param) {
        Long targetId = param.getLong(TARGET_ID_FIELD_NAME);
        Long coreModelId = param.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME);
        return new SQL().SELECT("id, model_id, core_model_id, file_name").FROM(TABLE_NAME)
                .WHERE(CharSequenceUtil.format("target_id={} and core_model_id={}", targetId, coreModelId))
                .toString();
    }

    public String getMediaByCondition(Dict param) {
        final SQL sql = new SQL().SELECT(DB_FIELDS).FROM(TABLE_NAME);
        if (null != param.getLong(TARGET_ID_FIELD_NAME)) {
            sql.WHERE(CharSequenceUtil.format("target_id = {}", param.getLong(TARGET_ID_FIELD_NAME)));
        }
        if (null != param.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME)) {
            sql.WHERE(CharSequenceUtil.format("core_model_id = {}", param.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME)));
        }
        if (null != param.getStr("resource_type")) {
            sql.WHERE(CharSequenceUtil.format("resource_type = '{}'", param.getStr("resource_type")));
        }
        if (null != param.getStr("order_by")) {
            sql.ORDER_BY(param.getStr("order_by"));
        }
        if (null != param.getInt("limit")) {
            sql.LIMIT(param.getInt("limit"));
        }
        return sql.toString();
    }
}
