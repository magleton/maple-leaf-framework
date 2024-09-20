package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

public class GXUpdateJsonSetStrField extends GXUpdateField<String> {
    private final String path;

    public GXUpdateJsonSetStrField(String tableNameAlias, String fieldName, String path, String value) {
        super(tableNameAlias, fieldName, value);
        this.path = path;
    }

    @Override
    public String getFieldValue() {
        String strValue = value.toString();
        if (CharSequenceUtil.contains(strValue, "'") && JSONUtil.isTypeJSON(strValue)) {
            strValue = CharSequenceUtil.format("CAST('{}' as JSON)", CharSequenceUtil.replace(strValue, "'", "''"));
            return CharSequenceUtil.format("JSON_SET({}.{} , '$.{}' , {})", tableNameAlias, fieldName, path, strValue);
        }
        strValue = CharSequenceUtil.replace(strValue, "'", "\\'");
        return CharSequenceUtil.format("JSON_SET({}.{} , '$.{}' , '{}')", tableNameAlias, fieldName, path, strValue);
    }
}
