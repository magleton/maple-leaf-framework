package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

import java.io.Serializable;

public abstract class GXUpdateField<T> implements Serializable {
    protected String tableNameAlias;

    protected String fieldName;

    @SuppressWarnings("all")
    protected Object value;

    protected GXUpdateField(String tableNameAlias, String fieldName, Object value) {
        this.tableNameAlias = tableNameAlias;
        this.fieldName = fieldName;
        this.value = value;
    }

    public String updateString() {
        if (CharSequenceUtil.isEmpty(tableNameAlias)) {
            return CharSequenceUtil.format("`{}` = {}", fieldName, getFieldValue());
        }
        return CharSequenceUtil.format("`{}`.`{}` = {}", tableNameAlias, CharSequenceUtil.toUnderlineCase(fieldName), getFieldValue());
    }

    public abstract T getFieldValue();
}