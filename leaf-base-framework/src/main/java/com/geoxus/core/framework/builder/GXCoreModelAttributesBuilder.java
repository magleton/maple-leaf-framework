package com.geoxus.core.framework.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import org.apache.ibatis.jdbc.SQL;

public class GXCoreModelAttributesBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        return "";
    }

    public String getModelAttributesByModelId(Dict param) {
        final SQL sql = new SQL().SELECT("" +
                "ca.ext as c_ext, ca.attribute_name, ca.attribute_id, ca.show_name, ca.category, ca.data_type,\n" +
                "ca.front_type, ca.validation_desc, ca.validation_expression, cmaa.error_tips, cmaa.force_validation, cmaa.default_value, \n" +
                "cmaa.fixed_value, cmaa.table_field_name, cmaa.required, cmaa.ext as cm_ext , cmaa.is_auto_generation")
                .FROM("core_model_attributes as cmaa");
        sql.INNER_JOIN("core_model_table_field as cmtf on cmaa.core_model_table_field_id=cmtf.core_model_table_field_id");
        sql.LEFT_OUTER_JOIN("core_attributes ca on cmaa.attribute_id = ca.attribute_id");
        sql.INNER_JOIN("core_model on  core_model.model_id = cmaa.core_model_id");
        sql.WHERE(StrUtil.format("cmaa.core_model_id={core_model_id}", param));
        if (CharSequenceUtil.isNotBlank(param.getStr("table_field_name"))) {
            sql.WHERE(StrUtil.format("cmtf.table_field_name = '{table_field_name}'", param));
        }
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        return null;
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
        return null;
    }

    @Override
    public String getModelIdentificationValue() {
        return null;
    }
}
