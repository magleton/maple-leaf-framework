package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

public class GXUpdateStrField extends GXUpdateField<String> {
    public GXUpdateStrField(String tableNameAlias, String fieldName, String strValue) {
        super(tableNameAlias, fieldName, strValue);
    }

    @Override
    public String getFieldValue() {
        String strValue = value.toString();
        if (CharSequenceUtil.contains(strValue, "'")) {
            if (JSONUtil.isTypeJSON(strValue)) {
                strValue = CharSequenceUtil.replace(strValue, "'", "''");
            } else {
                strValue = CharSequenceUtil.replace(strValue, "'", "\\'");
            }
        }
        return CharSequenceUtil.format("'{}'", strValue);
    }
}
