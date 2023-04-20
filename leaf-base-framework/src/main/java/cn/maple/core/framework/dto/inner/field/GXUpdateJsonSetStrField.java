package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

public class GXUpdateJsonSetStrField extends GXUpdateField<String> {
    private final String path;

    public GXUpdateJsonSetStrField(String tableNameAlias, String fieldName, String path, String value) {
        super(tableNameAlias, fieldName, value);
        this.path = path;
    }

    @Override
    public String getFieldValue() {
        return CharSequenceUtil.format("JSON_SET(`{}`.`{}` , '$.{}' , {})", tableNameAlias, fieldName, path, CharSequenceUtil.format("\"{}\"", value));
    }
}
