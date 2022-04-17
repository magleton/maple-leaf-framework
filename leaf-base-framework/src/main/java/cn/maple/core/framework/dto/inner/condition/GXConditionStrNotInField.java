package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;

import java.util.Set;
import java.util.stream.Collectors;

public class GXConditionStrNotInField extends GXCondition<String> {
    public GXConditionStrNotInField(String tableNameAlias, String fieldName, Set<String> value) {
        super(tableNameAlias, fieldName, () -> {
            String str = value.stream().map(v -> CharSequenceUtil.format("'{}'", v)).collect(Collectors.joining(","));
            return CharSequenceUtil.format("({})", str);
        });
    }

    @Override
    String getOp() {
        return "not in";
    }
}
