package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

public class GXUpdateJsonSetStrField extends GXUpdateField<String> {
    private final String jsonPathFieldName;

    public GXUpdateJsonSetStrField(String tableNameAlias, String fieldName, String jsonPathFieldName, String value) {
        super(tableNameAlias, fieldName, value);
        this.jsonPathFieldName = jsonPathFieldName;
    }

    @Override
    public String getFieldValue() {
        return CharSequenceUtil.format("JSON_SET(`{}`.`{}` , '$.{}' , {})", tableNameAlias, fieldName, jsonPathFieldName, CharSequenceUtil.format("'{}'", value));
    }
}
