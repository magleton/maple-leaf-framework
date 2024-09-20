package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.exception.GXUpdateFieldFormatException;

public class GXUpdateJsonSetStrField extends GXUpdateField<String> {
    private final String path;

    public GXUpdateJsonSetStrField(String tableNameAlias, String fieldName, String path, String value) {
        super(tableNameAlias, fieldName, value);
        this.path = path;
    }

    @Override
    public String getFieldValue() {
        String strValue = value.toString();
        if (JSONUtil.isTypeJSON(strValue)) {
            throw new GXUpdateFieldFormatException("不能使用JSON格式的字符串");
        }
        if (CharSequenceUtil.contains(strValue, "'")) {
            return CharSequenceUtil.format("JSON_SET({}.{} , '$.{}' , \"{}\")", tableNameAlias, fieldName, path, strValue);
        }
        return CharSequenceUtil.format("JSON_SET({}.{} , '$.{}' , '{}')", tableNameAlias, fieldName, path, strValue);
    }
}
