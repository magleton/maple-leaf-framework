package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.exception.GXUpdateFieldFormatException;
import cn.maple.core.framework.util.GXCommonUtils;

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
        Boolean tenantLine = GXCommonUtils.getEnvironmentValue("maple.framework.enable.tenant-line", Boolean.class, Boolean.FALSE);
        if (tenantLine && CharSequenceUtil.contains(strValue, "'")) {
            throw new GXUpdateFieldFormatException("JSON字符串中包含【'】,请将其转换为Html实体表示！！！");
        }
        if (CharSequenceUtil.isEmpty(path)) {
            path = "$";
        } else {
            path = CharSequenceUtil.format("$.{}", path);
        }
        if (CharSequenceUtil.contains(strValue, "'")) {
            strValue = CharSequenceUtil.format("CAST('{}' as JSON)", CharSequenceUtil.replace(strValue, "'", "\\'"));
            return CharSequenceUtil.format("JSON_SET({} , '{}' , {})", fieldName, path, strValue);
        }
        strValue = CharSequenceUtil.format("CAST('{}' as JSON)", strValue);
        return CharSequenceUtil.format("JSON_SET({} , '{}' , {})", fieldName, path, strValue);
    }
}
