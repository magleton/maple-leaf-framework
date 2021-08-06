package com.geoxus.core.framework.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.Set;

public class GXCoreModelBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        return null;
    }

    @Override
    public String detail(Dict param) {
        return null;
    }

    public String getSearchCondition(Dict param) {
        final SQL sql = new SQL().SELECT("model_id , search_condition").FROM("core_model");
        final Set<Map.Entry<String, Object>> entrySet = getDefaultSearchField().entrySet();
        if (entrySet.isEmpty()) {
            return sql.WHERE("1 = 0").toString();
        }
        boolean whereConditionIsExists = false;
        for (Map.Entry<String, Object> entry : entrySet) {
            if (null != param.getObj(entry.getKey())) {
                whereConditionIsExists = true;
                sql.WHERE(CharSequenceUtil.format("{}".concat(entry.getValue().toString()), entry.getKey(), param.getObj(entry.getKey())));
            }
        }
        if (!whereConditionIsExists) {
            sql.WHERE(" 1 = 0");
        }
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set("model_id", GXBaseBuilderConstants.NUMBER_EQ)
                .set("model_name", GXBaseBuilderConstants.RIGHT_LIKE)
                .set("model_identification", GXBaseBuilderConstants.STR_EQ)
                .set("model_type", GXBaseBuilderConstants.RIGHT_LIKE)
                .set("table_name", GXBaseBuilderConstants.STR_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return "core_model";
    }
}
