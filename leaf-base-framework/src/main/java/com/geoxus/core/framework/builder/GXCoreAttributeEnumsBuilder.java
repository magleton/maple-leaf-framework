package com.geoxus.core.framework.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.framework.constant.GXCoreAttributeEnumsConstant;
import org.apache.ibatis.jdbc.SQL;

public class GXCoreAttributeEnumsBuilder implements GXBaseBuilder {
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("cae.*").FROM(CharSequenceUtil.format("{} as cae", GXCoreAttributeEnumsConstant.TABLE_NAME));
        sql.INNER_JOIN("core_attributes ca on cae.attribute_id = ca.attribute_id");
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("core_attributes_enums.*").FROM(CharSequenceUtil.format("{} as cae", GXCoreAttributeEnumsConstant.TABLE_NAME));
        sql.WHERE(StrUtil.format("attribute_enum_id = {attribute_enum_id}", param));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set("cae.core_model_id", GXBaseBuilderConstants.NUMBER_EQ)
                .set("cae.attribute_id", GXBaseBuilderConstants.NUMBER_EQ)
                .set("ca.attribute_name", GXBaseBuilderConstants.RIGHT_LIKE)
                .set("ca.show_name", GXBaseBuilderConstants.RIGHT_LIKE)
                .set("ca.category", GXBaseBuilderConstants.RIGHT_LIKE);
    }

    @Override
    public String getModelIdentificationValue() {
        return GXCoreAttributeEnumsConstant.MODEL_IDENTIFICATION_VALUE;
    }

    public String exists(Dict param) {
        final SQL sql = new SQL().SELECT("count(*) as cnt").FROM(GXCoreAttributeEnumsConstant.TABLE_NAME);
        sql.WHERE(StrUtil.format("attribute_id = {attribute_id} and core_model_id = {core_model_id}", param));
        return sql.toString();
    }

    /**
     * 根据属性ID和核心模型ID获取数据
     *
     * @param attributeId 属性ID
     * @param coreModelId 核心模型ID
     * @return String
     */
    public String getAttributeEnumsByAttributeIdAndCoreModelId(Integer attributeId, Integer coreModelId) {
        final SQL sql = new SQL().SELECT("cae.*").FROM(CharSequenceUtil.format("{} as cae", GXCoreAttributeEnumsConstant.TABLE_ALIAS_NAME));
        sql.INNER_JOIN("core_attributes ca on cae.attribute_id = ca.attribute_id");
        sql.WHERE(CharSequenceUtil.format("cae.attribute_id = {} AND cae.core_model_id = {}", attributeId, coreModelId));
        return sql.toString();
    }
}
