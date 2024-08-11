package cn.maple.debezium.exception;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.code.GXResultStatusCode;
import cn.maple.core.framework.exception.GXBusinessException;

public class GXDebeziumInitialException extends GXBusinessException {
    public GXDebeziumInitialException(GXResultStatusCode resultCode, String msg, Dict data, Throwable e) {
        super(resultCode, msg, data, e);
    }

    public GXDebeziumInitialException(GXResultStatusCode resultCode, Throwable e, Dict data) {
        super(resultCode, e, data);
    }

    public GXDebeziumInitialException(GXResultStatusCode resultCode, String msg, Dict data) {
        super(resultCode, msg, data);
    }

    public GXDebeziumInitialException(GXResultStatusCode resultCode, Throwable e) {
        super(resultCode, e);
    }

    public GXDebeziumInitialException(GXResultStatusCode resultCode, String msg) {
        super(resultCode, msg);
    }

    public GXDebeziumInitialException(GXResultStatusCode resultCode) {
        super(resultCode);
    }

    public GXDebeziumInitialException(String msg, Throwable e) {
        super(msg, e);
    }

    public GXDebeziumInitialException(String msg) {
        super(msg);
    }

    public GXDebeziumInitialException(String msg, int code) {
        super(msg, code);
    }

    public GXDebeziumInitialException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXDebeziumInitialException(String msg, int code, Dict data) {
        super(msg, code, data);
    }

    public GXDebeziumInitialException(String msg, int code, Dict data, Throwable e) {
        super(msg, code, data, e);
    }
}
