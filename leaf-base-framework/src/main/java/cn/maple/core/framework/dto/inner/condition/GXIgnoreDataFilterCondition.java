package cn.maple.core.framework.dto.inner.condition;

/**
 * 标记类
 * 用于处理不需要数据权限验证的情况
 */
public class GXIgnoreDataFilterCondition extends GXCondition<String> {
    public GXIgnoreDataFilterCondition(String tableNameAlias, String fieldName, String value) {
        super(tableNameAlias, fieldName, value);
    }

    public GXIgnoreDataFilterCondition() {
        this("", "", "");
    }

    @Override
    public String getOp() {
        return "";
    }
    
    @Override
    public String getFieldValue() {
        return "";
    }
}
