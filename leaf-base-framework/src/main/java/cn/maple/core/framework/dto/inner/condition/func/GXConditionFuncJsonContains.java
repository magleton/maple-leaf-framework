package cn.maple.core.framework.dto.inner.condition.func;

import cn.hutool.core.text.CharSequenceUtil;

import java.util.List;
import java.util.stream.Collectors;

public class GXConditionFuncJsonContains extends GXConditionFunc<String> {
    private final List<String> values;

    private final String jsonPath;

    public GXConditionFuncJsonContains(String tableNameAlias, String op, List<String> values) {
        this(tableNameAlias, op, values, "$");
    }

    public GXConditionFuncJsonContains(String tableNameAlias, String op, List<String> values, String jsonPath) {
        super(tableNameAlias, op, "", null);
        this.values = values;
        this.jsonPath = jsonPath;
    }

    @Override
    public String getOp() {
        return op;
    }

    @Override
    public String getFieldExpression() {
        String format = "`{}`.`{}`->'" + jsonPath + "', CAST('[{}]' AS JSON)";
        return CharSequenceUtil.format(format, tableNameAlias);
    }

    @Override
    public String getFieldValue() {
        return values.stream().map(s -> CharSequenceUtil.format("\"{}\"", s)).collect(Collectors.joining(","));
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