package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.code.GXResultStatusCode;

public class GXDBConditionException extends GXBusinessException {
    public GXDBConditionException(String msg, int code, Dict data, Throwable e) {
        super(msg, code, data, e);
    }

    public GXDBConditionException(String msg, int code, Dict data) {
        super(msg, code, data);
    }

    public GXDBConditionException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXDBConditionException(String msg, int code) {
        super(msg, code);
    }

    public GXDBConditionException(String msg) {
        super(msg);
    }

    public GXDBConditionException(String msg, Throwable e) {
        super(msg, e);
    }

    public GXDBConditionException(GXResultStatusCode resultCode) {
        super(resultCode);
    }

    public GXDBConditionException(GXResultStatusCode resultCode, String msg) {
        super(resultCode, msg);
    }

    public GXDBConditionException(GXResultStatusCode resultCode, Throwable e) {
        super(resultCode, e);
    }

    public GXDBConditionException(GXResultStatusCode resultCode, String msg, Dict data) {
        super(resultCode, msg, data);
    }

    public GXDBConditionException(GXResultStatusCode resultCode, Throwable e, Dict data) {
        super(resultCode, e, data);
    }

    public GXDBConditionException(GXResultStatusCode resultCode, String msg, Dict data, Throwable e) {
        super(resultCode, msg, data, e);
    }
}
