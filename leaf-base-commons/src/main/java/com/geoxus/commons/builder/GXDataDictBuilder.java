package com.geoxus.commons.builder;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.commons.constant.GXDataDictConstant;
import com.geoxus.core.common.builder.GXBaseBuilder;
import org.apache.ibatis.jdbc.SQL;

public class GXDataDictBuilder implements GXBaseBuilder {
    /**
     * 列表
     *
     * @param param 参数
     * @return String
     */
    @Override
    public String listOrSearch(Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(GXDataDictConstant.TABLE_NAME);
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
    }

    @Override
    public String listOrSearchPage(IPage<Dict> page, Dict param) {
        final SQL sql = new SQL().SELECT("*").FROM(GXDataDictConstant.TABLE_NAME);
        mergeSearchConditionToSQL(sql, param);
        return sql.toString();
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
        return GXDataDictConstant.MODEL_IDENTIFICATION_VALUE;
    }
}
