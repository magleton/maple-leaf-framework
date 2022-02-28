package cn.maple.core.framework.exception;

import cn.maple.core.framework.code.GXHttpStatusCode;

public class GXTokenEmptyException extends GXBusinessException {
    public GXTokenEmptyException(String msg) {
        super(msg);
    }

    public GXTokenEmptyException(String msg, Throwable e) {
        super(msg, e);
    }

    public GXTokenEmptyException(String msg, int code) {
        super(msg, code);
    }

    public GXTokenEmptyException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXTokenEmptyException(GXHttpStatusCode resultCode) {
        super(resultCode);
    }

    public GXTokenEmptyException(GXHttpStatusCode resultCode, Throwable e) {
        super(resultCode, e);
    }
}
