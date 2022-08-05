package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;

public class GXConditionOrigin extends GXCondition<String> {
    public GXConditionOrigin(String value) {
        super("", "", value);
    }

    @Override
    public String getOp() {
        return "";
    }

    @Override
    public String getFieldValue() {
        return CharSequenceUtil.format("{}", value);
    }
}
