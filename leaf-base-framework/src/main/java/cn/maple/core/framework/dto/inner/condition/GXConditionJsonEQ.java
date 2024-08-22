package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.maple.core.framework.exception.GXSqlInjectionException;
import cn.maple.core.framework.util.GXDBStringEscapeUtils;

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
        if (GXDBStringEscapeUtils.check(value.toString())) {
            throw new GXSqlInjectionException("SQL注入异常");
        }
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
