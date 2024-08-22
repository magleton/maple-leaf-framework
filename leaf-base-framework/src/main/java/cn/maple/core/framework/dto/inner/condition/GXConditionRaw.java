package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GXConditionRaw extends GXCondition<String> {
    public GXConditionRaw(String value) {
        super("", "", value);
    }

    @Override
    public String getOp() {
        return "";
    }

    @Override
    public String getFieldValue() {
        log.info("~~请确保使用GXDBStringEscapeUtils.check(str)函数对用户传入的数据进行了SQL注入检测~~");
        return CharSequenceUtil.format("{}", value);
    }
}
