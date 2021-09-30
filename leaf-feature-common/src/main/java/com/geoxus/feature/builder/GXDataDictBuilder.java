package com.geoxus.feature.builder;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.feature.constant.GXDataDictConstant;
import com.geoxus.core.builder.GXBaseBuilder;
import org.apache.ibatis.jdbc.SQL;

public class GXDataDictBuilder implements GXBaseBuilder {
    /**
     * 列表
     *
     * @param param 参数
     * @return String
     */
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(GXDataDictConstant.TABLE_NAME);
        mergeSearchConditionToSql(sql, param, false);
        return sql.toString();
    }

    @Override
    public <R> String listOrSearchPage(IPage<R> page, Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(GXDataDictConstant.TABLE_NAME);
        mergeSearchConditionToSql(sql, param, false);
        return sql.toString();
    }

    /**
     * 数据配置的模型标识
     *
     * @return String
     */
    @Override
    public String getModelIdentificationValue() {
        return GXDataDictConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
