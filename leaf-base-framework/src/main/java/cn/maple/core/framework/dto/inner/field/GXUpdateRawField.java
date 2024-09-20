package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.exception.GXUpdateFieldFormatException;
import cn.maple.core.framework.util.GXCommonUtils;

public class GXUpdateRawField extends GXUpdateField<String> {
    public GXUpdateRawField(String tableNameAlias, String fieldName, String strValue) {
        super(tableNameAlias, fieldName, strValue);
    }

    @Override
    public String getFieldValue() {
        String strValue = value.toString();
        if (JSONUtil.isTypeJSON(strValue)) {
            Boolean tenantLine = GXCommonUtils.getEnvironmentValue("maple.framework.enable.tenant-line", Boolean.class, Boolean.FALSE);
            if (tenantLine && CharSequenceUtil.contains(strValue, "'")) {
                throw new GXUpdateFieldFormatException("JSON字符串中包含【'】,请将其转换为Html实体表示！！！");
            }
        }
        return CharSequenceUtil.format("{}", strValue);
    }
}
