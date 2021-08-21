package com.geoxus.core.framework.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.framework.constant.GXCoreModelAttributesConstant;
import org.apache.ibatis.jdbc.SQL;

import java.util.HashSet;

public class GXCoreModelAttributesBuilder implements GXBaseBuilder {
    public String getModelAttributesByCondition(Dict param) {
        HashSet<String> selectColumns = CollUtil.newHashSet();
        selectColumns.add("core_attributes.ext as c_ext");
        selectColumns.add("core_attributes.attribute_name");
        selectColumns.add("core_attributes.attribute_id");
        selectColumns.add("core_attributes.show_name");
        selectColumns.add("core_attributes.category");
        selectColumns.add("core_attributes.data_type");
        selectColumns.add("core_attributes.front_type");
        selectColumns.add("core_attributes.validation_desc");
        selectColumns.add("core_attributes.validation_expression");
        selectColumns.add("core_model_attributes.error_tips");
        selectColumns.add("core_model_attributes.force_validation");
        selectColumns.add("core_model_attributes.default_value");
        selectColumns.add("core_model_attributes.fixed_value");
        selectColumns.add("core_model_attributes.table_field_name");
        selectColumns.add("core_model_attributes.required");
        selectColumns.add("core_model_attributes.ext as cm_ext");
        selectColumns.add("core_model_attributes.is_auto_generation");
        final SQL sql = new SQL().SELECT(String.join(",", selectColumns))
                .FROM(CharSequenceUtil.format("{}", GXCoreModelAttributesConstant.TABLE_NAME));
        sql.INNER_JOIN("core_model_table_field on core_model_attributes.core_model_table_field_id=core_model_table_field.core_model_table_field_id");
        sql.LEFT_OUTER_JOIN("core_attributes on core_model_attributes.attribute_id=core_attributes.attribute_id");
        sql.INNER_JOIN("core_model on core_model.model_id=core_model_attributes.core_model_id");
        sql.WHERE(StrUtil.format("core_model_attributes.core_model_id={core_model_id}", param));
        if (CharSequenceUtil.isNotBlank(param.getStr("table_field_name"))) {
            sql.WHERE(StrUtil.format("core_model_table_field.table_field_name = '{table_field_name}'", param));
        }
        return sql.toString();
    }

    /**
     * 检测指定模型是否包含指定的属性
     *
     * @param param 参数
     * @return
     */
    public String checkCoreModelHasAttribute(Dict param) {
        String mainSql = "SELECT IFNULL(({}), 0)";
        String subSql = "SELECT 1 FROM core_attributes";
        subSql = subSql.concat("\nINNER JOIN core_model_attributes on core_model_attributes.attribute_id=core_attributes.attribute_id");
        subSql = subSql.concat("\nWHERE (core_attributes.attribute_name = '{attribute_name}' AND core_model_attributes.core_model_id = {core_model_id}) LIMIT 1");
        return CharSequenceUtil.format(mainSql, StrUtil.format(subSql, param));
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return GXCoreModelAttributesConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
