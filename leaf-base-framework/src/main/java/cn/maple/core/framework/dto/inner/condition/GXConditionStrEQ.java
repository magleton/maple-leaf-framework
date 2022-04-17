package cn.maple.core.framework.dto.inner.condition;

public class GXConditionStrEQ extends GXCondition {
    public GXConditionStrEQ(String tableNameAlias, String fieldName, String value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getOp() {
        return "=";
    }

    @Override
    public String getValueFormat() {
        return "'{}'";
    }
}
