package cn.maple.core.framework.dto.inner.condition;

public class GXConditionExclusionDeletedField extends GXCondition<String> {
    public GXConditionExclusionDeletedField(String tableNameAlias, String fieldName, String value) {
        super(tableNameAlias, fieldName, value);
    }

    public GXConditionExclusionDeletedField(String tableNameAlias, String fieldName, Long value) {
        super(tableNameAlias, fieldName, value);
    }

    public GXConditionExclusionDeletedField() {
        this("", "", "");
    }

    @Override
    public String getOp() {
        return null;
    }

    @Override
    public String getFieldValue() {
        return null;
    }
}
