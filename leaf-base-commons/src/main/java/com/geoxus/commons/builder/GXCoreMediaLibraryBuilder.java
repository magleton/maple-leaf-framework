package com.geoxus.commons.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXCommonConstants;
import org.apache.ibatis.jdbc.SQL;

@SuppressWarnings("unused")
public class GXCoreMediaLibraryBuilder implements GXBaseBuilder {
    private static final String DB_FIELDS = "id, core_model_id, model_type, object_id, collection_name, name, file_name, size, manipulations, custom_properties, responsive_images, order_column, resource_type";

    @Override
    public String listOrSearch(Dict param) {
        return new SQL().SELECT("*").FROM("core_media_library").WHERE(StrUtil.format("model_id={} and core_model_id={}", param.getLong("model_id"), param.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME))).toString();
    }

    @Override
    public String detail(Dict param) {
        return new SQL().SELECT("*").FROM("core_media_library")
                .WHERE(StrUtil.format("model_id={} and core_model_id={}", param.getLong("model_id"), param.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME)))
                .toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return "core_media_library";
    }

    public String deleteByCondition(Dict param) {
        final SQL sql = new SQL().DELETE_FROM("core_media_library").WHERE(StrUtil.format("model_id = {} and core_model_id = {}", param.getLong("model_id"), param.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME)));
        return sql.toString();
    }

    public String baseInfoDetail(Dict param) {
        return new SQL().SELECT("id, model_id, core_model_id, file_name").FROM("core_media_library")
                .WHERE(CharSequenceUtil.format("model_id={} and core_model_id={}", param.getLong("model_id"), param.getLong(GXCommonConstants.CORE_MODEL_PRIMARY_FIELD_NAME)))
                .toString();
    }

    public String getMediaByCondition(Dict param) {
        final SQL sql = new SQL().SELECT(DB_FIELDS).FROM("core_media_library");
        if (null != param.getLong("object_id")) {
            sql.WHERE(CharSequenceUtil.format("object_id = {}", param.getLong("object_id")));
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
