package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.code.GXResultStatusCode;

public class GXDBExistsException extends GXBusinessException {
    public GXDBExistsException(String msg, int code, Dict data, Throwable e) {
        super(msg, code, data, e);
    }

    public GXDBExistsException(String msg, int code, Dict data) {
        super(msg, code, data);
    }

    public GXDBExistsException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXDBExistsException(String msg, int code) {
        super(msg, code);
    }

    public GXDBExistsException(String msg) {
        super(msg);
    }

    public GXDBExistsException(String msg, Throwable e) {
        super(msg, e);
    }

    public GXDBExistsException(GXResultStatusCode resultCode) {
        super(resultCode);
    }

    public GXDBExistsException(GXResultStatusCode resultCode, String msg) {
        super(resultCode, msg);
    }

    public GXDBExistsException(GXResultStatusCode resultCode, Throwable e) {
        super(resultCode, e);
    }

    public GXDBExistsException(GXResultStatusCode resultCode, String msg, Dict data) {
        super(resultCode, msg, data);
    }

    public GXDBExistsException(GXResultStatusCode resultCode, Throwable e, Dict data) {
        super(resultCode, e, data);
    }

    public GXDBExistsException(GXResultStatusCode resultCode, String msg, Dict data, Throwable e) {
        super(resultCode, msg, data, e);
    }
}
