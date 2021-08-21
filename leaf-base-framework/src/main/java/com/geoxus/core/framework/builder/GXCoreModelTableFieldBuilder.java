package com.geoxus.core.framework.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.framework.constant.GXCoreModelAttributesConstant;
import com.geoxus.core.framework.constant.GXCoreModelTableFieldConstant;
import org.apache.ibatis.jdbc.SQL;

import java.util.HashSet;

public class GXCoreModelTableFieldBuilder implements GXBaseBuilder {
    /**
     * 通过条件查询数据
     *
     * @param param 条件
     * @return String
     */
    public String getModelAttributesByCondition(Dict param) {
        HashSet<String> selectColumns = CollUtil.newHashSet();
        selectColumns.add("cma.model_attributes_id");
        selectColumns.add("cma.core_model_table_field_id");
        selectColumns.add("cma.table_field_name");
        selectColumns.add("cma.attribute_id");
        selectColumns.add("cma.required");
        selectColumns.add("cma.validation_expression");
        selectColumns.add("cma.force_validation");
        selectColumns.add("cma.fixed_value");
        selectColumns.add("ca.attribute_name");
        Integer coreModelId = param.getInt("core_model_id");
        String tableFieldName = param.getStr("table_field_name");
        final SQL sql = new SQL().SELECT(String.join(",", selectColumns))
                .FROM(CharSequenceUtil.format("{} as cma", GXCoreModelAttributesConstant.TABLE_NAME))
                .INNER_JOIN("core_model_table_field ON cma.core_model_table_field_id=core_model_table_field.core_model_table_field_id")
                .INNER_JOIN("core_attributes ca ON cma.attribute_id = ca.attribute_id");
        sql.WHERE(CharSequenceUtil.format("core_model_table_field.core_model_id = {} AND core_model_table_field.table_field_name = '{}'", coreModelId, tableFieldName));
        return sql.toString();
    }

    /**
     * 数据配置的模型标识
     *
     * @return String
     */
    @Override
    public String getModelIdentificationValue() {
        return GXCoreModelTableFieldConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
