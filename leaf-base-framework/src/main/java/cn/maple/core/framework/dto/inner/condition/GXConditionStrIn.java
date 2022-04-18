package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;

import java.util.Set;
import java.util.stream.Collectors;

public class GXConditionStrIn extends GXCondition<String> {
    public GXConditionStrIn(String tableNameAlias, String fieldName, Set<String> value) {
        super(tableNameAlias, fieldName, () -> {
            String str = value.stream().map(v -> CharSequenceUtil.format("'{}'", v)).collect(Collectors.joining(","));
            return CharSequenceUtil.format("({})", str);
        });
    }

    @Override
    public String getOp() {
        return "in";
    }
}
