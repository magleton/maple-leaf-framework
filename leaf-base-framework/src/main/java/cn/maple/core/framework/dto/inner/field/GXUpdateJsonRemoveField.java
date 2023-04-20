package cn.maple.core.framework.dto.inner.field;

import cn.hutool.core.text.CharSequenceUtil;

public class GXUpdateJsonRemoveField extends GXUpdateField<String> {
    private final String path;

    public GXUpdateJsonRemoveField(String tableNameAlias, String fieldName, String path) {
        super(tableNameAlias, fieldName, null);
        this.path = path;
    }

    @Override
    public String getFieldValue() {
        return CharSequenceUtil.format("JSON_REMOVE({} , \"$.{}\")", fieldName, path);
    }
}
