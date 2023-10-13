package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;

public class GXConditionJsonEQ extends GXCondition<String> {
    private final String jsonPath;

    public GXConditionJsonEQ(String tableNameAlias, String fieldName, String jsonFieldName, String value) {
        super(tableNameAlias, fieldName, value);
        this.jsonPath = CharSequenceUtil.format("$.{}", jsonFieldName);
    }

    @Override
    public String getOp() {
        return "=";
    }

    @Override
    public String getFieldValue() {
        if (NumberUtil.isNumber(value.toString())) {
            return CharSequenceUtil.format("{}", value);
        }
        return CharSequenceUtil.format("'{}'", value);
    }

    @Override
    public String getFieldExpression() {
        String format = "`{}`->'" + jsonPath + "'";
        return CharSequenceUtil.format(format, fieldExpression);
    }
}
