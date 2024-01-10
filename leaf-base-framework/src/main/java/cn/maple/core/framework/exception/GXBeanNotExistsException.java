package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;

public class GXBeanNotExistsException  extends GXBusinessException {
    public GXBeanNotExistsException(String msg, int code, Dict data, Throwable e) {
        super(msg, code, data, e);
    }

    public GXBeanNotExistsException(String msg) {
        super(msg);
    }
}
