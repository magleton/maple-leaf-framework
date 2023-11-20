package cn.maple.core.framework.dto.inner.condition.func;

import cn.hutool.core.text.CharSequenceUtil;

import java.util.List;
import java.util.stream.Collectors;

public class GXConditionFuncJsonContains extends GXConditionFunc<String> {
    private final List<Object> values;

    private String jsonPath;

    public GXConditionFuncJsonContains(String tableNameAlias, String jsonField, List<Object> values) {
        this(tableNameAlias, jsonField, values, "$");
    }

    public GXConditionFuncJsonContains(String tableNameAlias, String jsonField, List<Object> values, String jsonPath) {
        super(tableNameAlias, jsonField, "", null);
        this.values = values;
        this.jsonPath = jsonPath;
    }

    @Override
    public String getOp() {
        return op;
    }

    @Override
    public String getFieldExpression() {
        if (CharSequenceUtil.isEmpty(jsonPath)) {
            jsonPath = "$";
        } else {
            jsonPath = CharSequenceUtil.format("$.{}", jsonPath);
        }
        String format = "`{}`.`{}`->'" + jsonPath + "', CAST('[{}]' AS JSON)";
        return CharSequenceUtil.format(format, tableNameAlias);
    }

    @Override
    public String getFieldValue() {
        return values.stream().map(s -> {
            String format = "\"{}\"";
            if (s.getClass().isAssignableFrom(Integer.class) || s.getClass().isAssignableFrom(Long.class) || s.getClass().isAssignableFrom(Short.class)) {
                format = "{}";
            }
            return CharSequenceUtil.format(format, s);
        }).collect(Collectors.joining(","));
    }

    @Override
    protected String getFunctionName() {
        return "JSON_CONTAINS";
    }

    @Override
    public String whereString() {
        String format = CharSequenceUtil.format("{}({})", getFunctionName(), getFieldExpression());
        return CharSequenceUtil.format(format, getOp(), getFieldValue());
    }
}