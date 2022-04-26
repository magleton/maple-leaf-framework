package cn.maple.core.framework.dto.inner.condition.func;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.dto.inner.condition.GXCondition;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class GXConditionFunc<T> extends GXCondition<T> {
    protected String op;

    @SuppressWarnings("all")
    protected Object[] expression;

    protected GXConditionFunc(String tableNameAlias, String fieldName, Object value) {
        super(tableNameAlias, fieldName, value);
    }

    protected GXConditionFunc(String tableNameAlias, String op, Object value, Object... expression) {
        this(tableNameAlias, "", value);
        this.op = op;
        this.expression = expression;
    }

    @Override
    public String getFieldName() {
        return Arrays.stream(expression).map(o -> CharSequenceUtil.format("{}.{}", tableNameAlias, o)).collect(Collectors.joining(",'-',"));
    }

    protected abstract String getFunctionName();

    @Override
    public String whereString() {
        return CharSequenceUtil.format("{}({}) {} {}", getFunctionName(), getFieldName(), getOp(), getFieldValue());
    }
}
