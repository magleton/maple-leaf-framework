package com.geoxus.core.framework.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.framework.constant.GXCoreModelAttributesPermissionsConstant;
import org.apache.ibatis.jdbc.SQL;

import java.util.HashSet;

public class GXCoreModelAttributesPermissionsBuilder implements GXBaseBuilder {
    /**
     * 根据条件获取数据
     *
     * @param param 条件
     * @return String
     */
    public String getModelAttributePermissionByCondition(Dict param) {
        HashSet<String> selectColumns = CollUtil.newHashSet();
        selectColumns.add("core_model_attributes_permission.roles");
        selectColumns.add("core_model_attributes_permission.users");
        selectColumns.add("cma.table_field_name");
        selectColumns.add("ca.attribute_name");
        selectColumns.add("cma.attribute_id");
        selectColumns.add("cma.default_value");
        final SQL sql = new SQL().SELECT(String.join(",", selectColumns))
                .FROM(CharSequenceUtil.format("{}", GXCoreModelAttributesPermissionsConstant.TABLE_NAME));
        sql.INNER_JOIN("core_model_attributes cma ON cma.model_attributes_id=core_model_attributes_permission.model_attributes_id");
        sql.LEFT_OUTER_JOIN("core_attributes ca ON ca.attribute_id = cma.attribute_id");
        sql.WHERE(StrUtil.format("cma.core_model_id = {core_model_id}", param));
        if (null != param.getInt("parent_id")) {
            sql.WHERE(StrUtil.format("cma.parent_id = {parent_id}", param));
        }
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create();
    }

    @Override
    public String getModelIdentificationValue() {
        return GXCoreModelAttributesPermissionsConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
