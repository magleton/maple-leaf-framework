package cn.maple.core.framework.dto.inner.condition.func;

import cn.hutool.core.text.CharSequenceUtil;

public class GXConditionFuncConcat extends GXConditionFunc<String> {
    public GXConditionFuncConcat(String tableNameAlias, String op, String value, Object... expression) {
        super(tableNameAlias, op, value, expression);
    }

    @Override
    public String getOp() {
        return op;
    }

    @Override
    public String getFieldValue() {
        return CharSequenceUtil.format("'{}'", value);
    }

    @Override
    protected String getFunctionName() {
        return "concat";
    }
}
