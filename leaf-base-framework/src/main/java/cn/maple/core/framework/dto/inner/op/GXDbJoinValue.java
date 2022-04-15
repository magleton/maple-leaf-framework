package cn.maple.core.framework.dto.inner.op;

@SuppressWarnings("all")
public abstract class GXDbJoinValue extends GXDbJoinOp {
    private final String tableNameAlias;

    private final String fieldName;
    
    /**
     * 字段的值
     * 用于查询固定值的场景
     */
    protected Object fieldValue;

    public GXDbJoinValue(String tableNameAlias, String fieldName, Object fieldValue) {
        this.tableNameAlias = tableNameAlias;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public String opString() {
        return tableNameAlias + "." + fieldName + getOp() + fieldValue;
    }
}
