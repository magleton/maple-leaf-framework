package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.code.GXDefaultResultStatusCode;
import cn.maple.core.framework.code.GXResultStatusCode;

public class GXDBNotExistsException extends GXBusinessException {
    public GXDBNotExistsException(String msg, int code, Dict data, Throwable e) {
        super(msg, code, data, e);
    }

    public GXDBNotExistsException(String msg, int code, Dict data) {
        super(msg, code, data);
    }

    public GXDBNotExistsException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXDBNotExistsException(String msg, int code) {
        super(msg, code);
    }

    public GXDBNotExistsException(String msg) {
        super(msg);
    }

    public GXDBNotExistsException(String msg, Throwable e) {
        super(msg, e);
    }

    public GXDBNotExistsException(GXResultStatusCode resultCode) {
        super(resultCode);
    }

    public GXDBNotExistsException(GXResultStatusCode resultCode, String msg) {
        super(resultCode, msg);
    }

    public GXDBNotExistsException(GXResultStatusCode resultCode, Throwable e) {
        super(resultCode, e);
    }

    public GXDBNotExistsException(GXResultStatusCode resultCode, String msg, Dict data) {
        super(resultCode, msg, data);
    }

    public GXDBNotExistsException(GXResultStatusCode resultCode, Throwable e, Dict data) {
        super(resultCode, e, data);
    }

    public GXDBNotExistsException(GXResultStatusCode resultCode, String msg, Dict data, Throwable e) {
        super(resultCode, msg, data, e);
    }
}
