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

    public String getValueFormat() {
        return "{}";
    }

    public String whereString() {
        String format = CharSequenceUtil.format("{}.{} {} {}", tableNameAlias, fieldName, getOp(), getValueFormat());
        return CharSequenceUtil.format(format, value);
    }
}
