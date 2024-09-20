package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.exception.GXUpdateFieldFormatException;
import cn.maple.core.framework.util.GXCommonUtils;

import java.util.Map;

public class GXUpdateMapField<T extends Map<String, Object>> extends GXUpdateField<String> {
    public GXUpdateMapField(String tableNameAlias, String fieldName, T value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getFieldValue() {
        String strValue = JSONUtil.toJsonStr(value);
        Boolean tenantLine = GXCommonUtils.getEnvironmentValue("maple.framework.enable.tenant-line", Boolean.class, Boolean.FALSE);
        if (tenantLine && CharSequenceUtil.contains(strValue, "'")) {
            throw new GXUpdateFieldFormatException("JSON字符串中包含【'】,请将其转换为Html实体表示！！！");
        }
        if (CharSequenceUtil.contains(strValue, "'")) {
            strValue = CharSequenceUtil.replace(strValue, "'", "\\'");
            return CharSequenceUtil.format("'{}'", strValue);
        }
        return CharSequenceUtil.format("'{}'", strValue);
    }
}
