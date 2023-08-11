package cn.maple.sse;

import cn.maple.core.framework.exception.GXBusinessException;

public class GXSseBusinessException extends GXBusinessException {
    public GXSseBusinessException(String msg, Throwable e) {
        super(msg, e);
    }
}
