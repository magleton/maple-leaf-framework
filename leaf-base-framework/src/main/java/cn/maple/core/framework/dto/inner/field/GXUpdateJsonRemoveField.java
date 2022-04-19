package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

public class GXUpdateJsonRemoveField extends GXUpdateField<String> {
    private final String jsonPathFieldName;

    public GXUpdateJsonRemoveField(String tableNameAlias, String fieldName, String jsonPathFieldName) {
        super(tableNameAlias, fieldName, null);
        this.jsonPathFieldName = jsonPathFieldName;
    }

    @Override
    public String getFieldValue() {
        return CharSequenceUtil.format("JSON_REMOVE({} , '$.{}')", fieldName, jsonPathFieldName);
    }
}
