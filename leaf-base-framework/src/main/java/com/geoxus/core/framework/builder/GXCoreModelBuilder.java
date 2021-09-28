package com.geoxus.core.framework.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.constant.GXBaseBuilderConstant;
import com.geoxus.core.framework.constant.GXCoreModelConstant;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.Set;

public class GXCoreModelBuilder implements GXBaseBuilder {
    public String getSearchCondition(Dict param) {
        final SQL sql = new SQL().SELECT("model_id , search_condition").FROM(GXCoreModelConstant.TABLE_NAME);
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
                .set("modelId", GXBaseBuilderConstant.NUMBER_EQ)
                .set("modelName", GXBaseBuilderConstant.RIGHT_LIKE)
                .set("modelIdentification", GXBaseBuilderConstant.STR_EQ)
                .set("modelType", GXBaseBuilderConstant.RIGHT_LIKE)
                .set("tableName", GXBaseBuilderConstant.STR_EQ);
    }

    @Override
    public String getModelIdentificationValue() {
        return GXCoreModelConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
