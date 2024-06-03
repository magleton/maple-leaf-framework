package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXDBStringEscapeUtils;

public class GXConditionLikeFull extends GXCondition<String> {
    public GXConditionLikeFull(String tableNameAlias, String fieldName, String value) {
        super(tableNameAlias, fieldName, value);
    }

    @Override
    public String getOp() {
        return "like";
    }

    @Override
    public String getFieldValue() {
        String str = GXDBStringEscapeUtils.escapeRawString(value.toString());
        String format = "\"{}%\"";
        if (CharSequenceUtil.contains(str, "\"")) {
            format = "'{}%'";
        }
        return CharSequenceUtil.format(format, str);
    }
}
