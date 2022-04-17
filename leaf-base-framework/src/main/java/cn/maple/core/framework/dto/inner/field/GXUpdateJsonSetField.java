package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

public class GXUpdateJsonSetField extends GXUpdateField<String> {
    public GXUpdateJsonSetField(String tableNameAlias, String fieldName, String jsonPathFieldName, String value) {
        super(tableNameAlias, fieldName, () -> CharSequenceUtil.format("JSON_SET({} , '$.{}' , {})", fieldName, jsonPathFieldName, CharSequenceUtil.format("'{}'", value)));
    }
}
