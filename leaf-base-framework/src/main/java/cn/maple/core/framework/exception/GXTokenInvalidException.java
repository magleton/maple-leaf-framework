package cn.maple.core.framework.exception;

import cn.maple.core.framework.code.GXHttpStatusCode;

public class GXTokenInvalidException extends GXBusinessException {
    public GXTokenInvalidException(String msg) {
        super(msg);
    }

    public GXTokenInvalidException(String msg, Throwable e) {
        super(msg, e);
    }

    public GXTokenInvalidException(String msg, int code) {
        super(msg, code);
    }

    public GXTokenInvalidException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXTokenInvalidException(GXHttpStatusCode resultCode) {
        super(resultCode);
    }

    public GXTokenInvalidException(GXHttpStatusCode resultCode, Throwable e) {
        super(resultCode, e);
    }
}
