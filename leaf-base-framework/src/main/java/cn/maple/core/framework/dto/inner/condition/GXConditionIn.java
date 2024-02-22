package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GXConditionIn extends GXCondition<String> {
    public GXConditionIn(String tableNameAlias, String fieldName, Set<Number> value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getOp() {
        return "in";
    }

    @Override
    public String getFieldValue() {
        String activeProfile = GXCommonUtils.getActiveProfile();
        int limitCnt = 300;
        List<String> envLst = CollUtil.newArrayList(GXCommonConstant.RUN_ENV_DEV, GXCommonConstant.RUN_ENV_LOCAL);
        if (CollUtil.contains(envLst, activeProfile)) {
            limitCnt = 10;
        }
        if (CollUtil.size(value) > limitCnt) {
            throw new GXBusinessException(CharSequenceUtil.format("IN查询条件不能超过{}条数据!", limitCnt));
        }
        String str = ((Set<Number>) value).stream().map(String::valueOf).collect(Collectors.joining(","));
        return CharSequenceUtil.format("({})", str);
    }
}
