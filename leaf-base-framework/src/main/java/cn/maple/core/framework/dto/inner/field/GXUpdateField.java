package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

import java.io.Serializable;
import java.util.function.Supplier;

public abstract class GXUpdateField<T> implements Serializable {
    private final String tableNameAlias;

    private final String fieldName;

    protected Supplier<T> valueSupplier;

    protected GXUpdateField(String tableNameAlias, String fieldName, Supplier<T> valueSupplier) {
        this.tableNameAlias = tableNameAlias;
        this.fieldName = fieldName;
        this.valueSupplier = valueSupplier;
    }

    public String updateString() {
        if (CharSequenceUtil.isEmpty(tableNameAlias)) {
            return CharSequenceUtil.format("`{}` = {}", fieldName, valueSupplier.get());
        }
        return CharSequenceUtil.format("`{}`.`{}` = {}", tableNameAlias, CharSequenceUtil.toUnderlineCase(fieldName), valueSupplier.get());
    }
}