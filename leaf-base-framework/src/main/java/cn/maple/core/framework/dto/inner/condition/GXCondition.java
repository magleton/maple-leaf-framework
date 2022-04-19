package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;

import java.io.Serializable;

public abstract class GXCondition<T> implements Serializable {
    private final String tableNameAlias;

    private final String fieldName;

    @SuppressWarnings("all")
    protected Object value;

    protected GXCondition(String tableNameAlias, String fieldName, Object value) {
        this.tableNameAlias = tableNameAlias;
        this.fieldName = fieldName;
        this.value = value;
    }

    public abstract String getOp();

    public String whereString() {
        if (CharSequenceUtil.isEmpty(tableNameAlias)) {
            return CharSequenceUtil.format("{} {} {}", fieldName, getOp(), getFieldValue());
        }
        return CharSequenceUtil.format("{}.{} {} {}", tableNameAlias, CharSequenceUtil.toUnderlineCase(fieldName), getOp(), getFieldValue());
    }

    public String getFieldName() {
        return CharSequenceUtil.toUnderlineCase(fieldName);
    }

    public abstract T getFieldValue();
}
