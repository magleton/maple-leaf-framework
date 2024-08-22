package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.exception.GXSqlInjectionException;
import cn.maple.core.framework.util.GXDBStringEscapeUtils;

public class GXConditionLikeRight extends GXCondition<String> {
    public GXConditionLikeRight(String tableNameAlias, String fieldName, String value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getOp() {
        return "like";
    }

    @Override
    public String getFieldValue() {
        if (GXDBStringEscapeUtils.check(value.toString())) {
            throw new GXSqlInjectionException("SQL注入异常");
        }
        String str = GXDBStringEscapeUtils.escapeRawString(value.toString());
        String format = "'{}%'";
        if (CharSequenceUtil.contains(str, "\\'")) {
            format = "\"{}%\"";
        }
        return CharSequenceUtil.format(format, str);
    }
}
