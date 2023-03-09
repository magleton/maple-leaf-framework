package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

public class GXUpdateRawField extends GXUpdateField<String> {
    public GXUpdateRawField(String tableNameAlias, String fieldName, String strValue) {
        super(tableNameAlias, fieldName, strValue);
    }

    @Override
    public String getFieldValue() {
        String strValue = value.toString();
        if (JSONUtil.isTypeJSON(strValue)) {
            strValue = JSONUtil.quote(strValue, false);
        }
        return CharSequenceUtil.format("{}", strValue);
    }
}
