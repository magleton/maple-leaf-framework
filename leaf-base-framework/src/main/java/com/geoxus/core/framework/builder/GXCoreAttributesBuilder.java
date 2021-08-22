package com.geoxus.core.framework.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstant;
import com.geoxus.core.framework.constant.GXCoreAttributesConstant;
import org.apache.ibatis.jdbc.SQL;

public class GXCoreAttributesBuilder implements GXBaseBuilder {
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("ca.*").FROM(CharSequenceUtil.format("{} as ca", GXCoreAttributesConstant.TABLE_NAME));
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("ca.*").FROM(CharSequenceUtil.format("{} as ca", GXCoreAttributesConstant.TABLE_NAME));
        sql.WHERE(StrUtil.format("attribute_id = {attribute_id}", param));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set("ca.attributeName", GXBaseBuilderConstant.RIGHT_LIKE)
                .set("ca.showName", GXBaseBuilderConstant.RIGHT_LIKE)
                .set("ca.category", GXBaseBuilderConstant.RIGHT_LIKE);
    }

    @Override
    public String getModelIdentificationValue() {
        return GXCoreAttributesConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
