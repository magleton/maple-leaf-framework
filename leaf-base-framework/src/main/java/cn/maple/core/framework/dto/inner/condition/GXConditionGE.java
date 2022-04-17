package cn.maple.core.framework.dto.inner.condition;

public class GXConditionGE extends GXCondition<Number> {
    public GXConditionGE(String tableNameAlias, String fieldName, Number value) {
        super(tableNameAlias, fieldName, () -> value);
    }

    @Override
    String getOp() {
        return ">=";
    }
}
