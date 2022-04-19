package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

import java.util.Map;

public class GXUpdateJsonSetMapField<T extends Map<String, Object>> extends GXUpdateField<String> {
    private final String jsonPathFieldName;

    public GXUpdateJsonSetMapField(String tableNameAlias, String fieldName, String jsonPathFieldName, T value) {
        super(tableNameAlias, fieldName, value);
        this.jsonPathFieldName = jsonPathFieldName;
    }

    @Override
    public String getFieldValue() {
        String jsonValue = JSONUtil.toJsonStr(value);
        jsonValue = CharSequenceUtil.format("CAST('{}' as JSON)", jsonValue);
        return CharSequenceUtil.format("JSON_SET({} , '$.{}' , {})", fieldName, jsonPathFieldName, jsonValue);
    }
}
