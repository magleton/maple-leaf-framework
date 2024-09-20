package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

import java.util.Map;

public class GXUpdateJsonSetMapField<T extends Map<String, Object>> extends GXUpdateField<String> {
    private String path;

    public GXUpdateJsonSetMapField(String tableNameAlias, String fieldName, String path, T value) {
        super(tableNameAlias, fieldName, value);
        this.path = path;
    }

    @Override
    public String getFieldValue() {
        String strValue = JSONUtil.toJsonStr(this.value);
        if (CharSequenceUtil.isEmpty(path)) {
            path = "$";
        } else {
            path = CharSequenceUtil.format("$.{}", path);
        }
        if (CharSequenceUtil.contains(strValue, "'")) {
            strValue = CharSequenceUtil.format("CAST('{}' as JSON)", CharSequenceUtil.replace(strValue, "'", "''"));
            return CharSequenceUtil.format("JSON_SET({} , '{}' , {})", fieldName, path, strValue);
        }
        strValue = CharSequenceUtil.format("CAST('{}' as JSON)", strValue);
        return CharSequenceUtil.format("JSON_SET({} , '{}' , {})", fieldName, path, strValue);
    }
}
