package com.geoxus.core.framework.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import org.apache.ibatis.jdbc.SQL;

public class GXCoreModelTableFieldBuilder implements GXBaseBuilder {
    /**
     * 通过条件查询数据
     *
     * @param param 条件
     * @return String
     */
    public String getModelAttributesByModelId(Dict param) {
        final SQL sql = new SQL().SELECT("cma.model_attributes_id ,cma.core_model_table_field_id , cma.table_field_name , cma.attribute_id," +
                "cma.required , cma.validation_expression, cma.force_validation, cma.fixed_value , ca.attribute_name")
                .FROM("core_model_attributes cma")
                .INNER_JOIN("core_model_table_field cmtf ON cma.core_model_table_field_id=cmtf.core_model_table_field_id")
                .INNER_JOIN("core_attributes ca ON cma.attribute_id = ca.attribute_id");
        sql.WHERE(CharSequenceUtil.format("cmtf.core_model_id = {} AND cmtf.table_field_name = '{}'", param.getInt("core_model_id"), param.getStr("table_field_name")));
        return sql.toString();
    }

    /**
     * 列表
     *
     * @param param 参数
     * @return String
     */
    @Override
    public String listOrSearch(Dict param) {
        return null;
    }

    /**
     * 详情
     *
     * @param param 参数
     * @return String
     */
    @Override
    public String detail(Dict param) {
        return null;
    }

    /**
     * 数据配置的模型标识
     *
     * @return String
     */
    @Override
    public String getModelIdentificationValue() {
        return "core_model_table_field";
    }
}
