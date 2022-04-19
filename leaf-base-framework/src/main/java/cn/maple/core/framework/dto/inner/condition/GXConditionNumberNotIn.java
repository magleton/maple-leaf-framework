package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;

import java.util.Set;
import java.util.stream.Collectors;

public class GXConditionNumberNotIn extends GXCondition<String> {
    public GXConditionNumberNotIn(String tableNameAlias, String fieldName, Set<Number> value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getOp() {
        return "not in";
    }

    @Override
    public String getFieldValue() {
        String str = ((Set<Number>) value).stream().map(String::valueOf).collect(Collectors.joining(","));
        return CharSequenceUtil.format("({})", str);
    }
}
