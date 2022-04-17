package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;

public abstract class GXCondition {
    private final String tableNameAlias;

    private final String fieldName;

    private final Object value;

    protected GXCondition(String tableNameAlias, String fieldName, Object value) {
        this.tableNameAlias = tableNameAlias;
        this.fieldName = fieldName;
        this.value = value;
    }

    abstract String getOp();

    public String whereString() {
        return CharSequenceUtil.format("{}.{} {} {}", tableNameAlias, fieldName, getOp(), value);
    }
}
