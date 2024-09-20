package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

import java.util.Map;

public class GXUpdateMapField<T extends Map<String, Object>> extends GXUpdateField<String> {
    public GXUpdateMapField(String tableNameAlias, String fieldName, T value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getFieldValue() {
        String strValue = JSONUtil.toJsonStr(value);
        if (CharSequenceUtil.contains(strValue, "'")) {
            strValue = CharSequenceUtil.replace(strValue, "'", "''");
            return CharSequenceUtil.format("'{}'", strValue);
        }
        return CharSequenceUtil.format("'{}'", strValue);
    }
}
