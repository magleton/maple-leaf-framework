package cn.maple.core.framework.dto.inner.condition.func;

import cn.hutool.core.text.CharSequenceUtil;

import java.util.List;
import java.util.stream.Collectors;

public class GXConditionFuncJsonContains extends GXConditionFunc<String> {
    private final List<String> values;

    public GXConditionFuncJsonContains(String tableNameAlias, String op, List<String> values) {
        super(tableNameAlias, op, "", null);
        this.values = values;
    }

    @Override
    public String getOp() {
        return op;
    }

    @Override
    public String getFieldExpression() {
        return "`{}`->'$', CAST('[{}]' AS JSON)";
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