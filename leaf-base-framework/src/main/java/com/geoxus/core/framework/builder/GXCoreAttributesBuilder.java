package com.geoxus.core.framework.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import org.apache.ibatis.jdbc.SQL;

public class GXCoreAttributesBuilder implements GXBaseBuilder {
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("ca.*").FROM("core_attributes as ca");
        mergeSearchConditionToSQL(sql, param, "");
        return sql.toString();
    }

    @Override
    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("ca.*").FROM("core_attributes as ca");
        sql.WHERE(StrUtil.format("attribute_id = {attribute_id}", param));
        return sql.toString();
    }

    @Override
    public Dict getDefaultSearchField() {
        return Dict.create()
                .set("ca.attribute_name", GXBaseBuilderConstants.RIGHT_LIKE)
                .set("ca.show_name", GXBaseBuilderConstants.RIGHT_LIKE)
                .set("ca.category", GXBaseBuilderConstants.RIGHT_LIKE);
    }

    @Override
    public String getModelIdentificationValue() {
        return "core_attributes";
    }
}
