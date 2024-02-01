package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.exception.GXBusinessException;

import java.util.Set;
import java.util.stream.Collectors;

public class GXConditionStrIn extends GXCondition<String> {
    public GXConditionStrIn(String tableNameAlias, String fieldName, Set<String> value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getOp() {
        return "in";
    }

    @Override
    public String getFieldValue() {
        if (CollUtil.size(value) > 300) {
            throw new GXBusinessException("IN查询条件不能超过300条数据!");
        }
        String str = ((Set<String>) value).stream().map(v -> CharSequenceUtil.format("'{}'", v)).collect(Collectors.joining(","));
        return CharSequenceUtil.format("({})", str);
    }
}
