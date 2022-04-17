package cn.maple.core.framework.dto.inner.field;

public class GXUpdateNumberField extends GXUpdateField<Number> {
    public GXUpdateNumberField(String tableNameAlias, String fieldName, int numberValue) {
        super(tableNameAlias, fieldName, () -> numberValue);
    }
}
