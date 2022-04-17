package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

import java.util.function.Supplier;

public abstract class GXUpdateField<T> {
    private final String tableNameAlias;

    private final String fieldName;

    protected Supplier<T> value;

    protected GXUpdateField(String tableNameAlias, String fieldName, Supplier<T> value) {
        this.tableNameAlias = tableNameAlias;
        this.fieldName = fieldName;
        this.value = value;
    }

    public String updateString() {
        return CharSequenceUtil.format("{}.{} = {}", tableNameAlias, fieldName, value.get());
    }
}