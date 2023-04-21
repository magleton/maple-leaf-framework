package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

import java.util.Map;

public class GXUpdateJsonSetMapField<T extends Map<String, Object>> extends GXUpdateField<String> {
    private final String path;

    public GXUpdateJsonSetMapField(String tableNameAlias, String fieldName, String path, T value) {
        super(tableNameAlias, fieldName, value);
        this.path = path;
    }

    @Override
    public String getFieldValue() {
        String value = JSONUtil.toJsonStr(this.value);
        value = CharSequenceUtil.replace(value, "'", "\\'");
        value = CharSequenceUtil.format("CAST('{}' as JSON)", value);
        return CharSequenceUtil.format("JSON_SET({} , '$.{}' , {})", fieldName, path, value);
    }
}
