package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXDBStringEscapeUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GXConditionStrNotIn extends GXCondition<String> {
    public GXConditionStrNotIn(String tableNameAlias, String fieldName, Set<String> value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getOp() {
        return "not in";
    }

    @Override
    public String getFieldValue() {
        String activeProfile = GXCommonUtils.getActiveProfile();
        int limitCnt = 100000;
        List<String> envLst = CollUtil.newArrayList(GXCommonConstant.RUN_ENV_DEV, GXCommonConstant.RUN_ENV_LOCAL);
        if (CollUtil.contains(envLst, activeProfile)/* && GXCurrentRequestContextUtils.isHTTP()*/) {
            limitCnt = GXCommonUtils.getEnvironmentValue("db.in.limit.cnt", Integer.class, 50);
        }
        if (CollUtil.size(value) > limitCnt) {
            throw new GXBusinessException(CharSequenceUtil.format("IN查询条件不能超过{}条数据!", limitCnt));
        }
        String str = ((Set<String>) value).stream().map(v -> {
            String val = GXDBStringEscapeUtils.escapeRawString(v);
            String format = "'{}'";
            if (CharSequenceUtil.contains(val, "\\'")) {
                format = "\"{}\"";
            }
            return CharSequenceUtil.format(format, val);
        }).collect(Collectors.joining(","));
        return CharSequenceUtil.format("({})", str);
    }
}
