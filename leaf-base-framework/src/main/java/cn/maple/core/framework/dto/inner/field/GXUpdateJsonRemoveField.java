package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

public class GXUpdateJsonRemoveField extends GXUpdateField<String> {
    public GXUpdateJsonRemoveField(String tableNameAlias, String fieldName, String jsonPathFieldName) {
        super(tableNameAlias, fieldName, () -> CharSequenceUtil.format("JSON_REMOVE({} , '$.{}')", fieldName, jsonPathFieldName));
    }
}
