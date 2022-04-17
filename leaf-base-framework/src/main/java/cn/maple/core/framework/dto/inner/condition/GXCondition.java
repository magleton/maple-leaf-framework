package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;

import java.util.function.Supplier;

public abstract class GXCondition<T> {
    private final String tableNameAlias;

    private final String fieldName;
    private final Supplier<T> valueSupplier;

    protected GXCondition(String tableNameAlias, String fieldName, Supplier<T> valueSupplier) {
        this.tableNameAlias = tableNameAlias;
        this.fieldName = fieldName;
        this.valueSupplier = valueSupplier;
    }

    abstract String getOp();

    public String whereString() {
        if (CharSequenceUtil.isEmpty(tableNameAlias)) {
            return CharSequenceUtil.format("{} {} {}", fieldName, getOp(), valueSupplier.get());
        }
        return CharSequenceUtil.format("{}.{} {} {}", tableNameAlias, fieldName, getOp(), valueSupplier.get());
    }

    public String getFieldName() {
        return fieldName;
    }

    public Supplier<T> getValueSupplier() {
        return valueSupplier;
    }
}
