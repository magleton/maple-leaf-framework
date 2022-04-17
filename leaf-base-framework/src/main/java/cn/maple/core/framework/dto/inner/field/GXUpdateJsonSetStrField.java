package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

public class GXUpdateJsonSetStrField extends GXUpdateField<String> {
    public GXUpdateJsonSetStrField(String tableNameAlias, String fieldName, String jsonPathFieldName, String value) {
        super(tableNameAlias, fieldName, () -> CharSequenceUtil.format("JSON_SET(`{}`.`{}` , '$.{}' , {})", tableNameAlias, fieldName, jsonPathFieldName, CharSequenceUtil.format("'{}'", value)));
    }
}
