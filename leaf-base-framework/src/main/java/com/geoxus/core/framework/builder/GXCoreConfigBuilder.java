package com.geoxus.core.framework.builder;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.geoxus.core.common.builder.GXBaseBuilder;
import com.geoxus.core.framework.constant.GXCoreConfigConstant;
import org.apache.ibatis.jdbc.SQL;

public class GXCoreConfigBuilder implements GXBaseBuilder {
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(GXCoreConfigConstant.TABLE_NAME);
        if (null != param.getStr("type")) {
            sql.WHERE("type = '" + param.getStr("type") + "'");
        }
        return sql.toString();
    }

    public String detail(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(GXCoreConfigConstant.TABLE_NAME).WHERE(CharSequenceUtil.format("{} = {}", GXCoreConfigConstant.PRIMARY_KEY, param.getInt(GXCoreConfigConstant.PRIMARY_KEY)));
        return sql.toString();
    }

    /**
     * 数据配置的模型标识
     *
     * @return String
     */
    @Override
    public String getModelIdentificationValue() {
        return GXCoreConfigConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
