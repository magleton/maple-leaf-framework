package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

public class GXUpdateStrField extends GXUpdateField<String> {
    public GXUpdateStrField(String tableNameAlias, String fieldName, String strValue) {
        super(tableNameAlias, fieldName, strValue);
    }

    @Override
    public String getFieldValue() {
        return CharSequenceUtil.format("'{}'", value);
    }
}
