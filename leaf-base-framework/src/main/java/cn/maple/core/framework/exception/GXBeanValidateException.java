package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;

public class GXBeanValidateException extends GXBusinessException {

    public GXBeanValidateException(String msg, int code, Dict data) {
        super(msg, code, data);
    }
}
