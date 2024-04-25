package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.code.GXResultStatusCode;

public class GXSentinelFlowException extends GXBusinessException {
    public GXSentinelFlowException(String msg, int code, Dict data, Throwable e) {
        super(msg, code, data, e);
    }

    public GXSentinelFlowException(String msg, int code, Dict data) {
        super(msg, code, data);
    }

    public GXSentinelFlowException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXSentinelFlowException(String msg, int code) {
        super(msg, code);
    }

    public GXSentinelFlowException(String msg) {
        super(msg);
    }

    public GXSentinelFlowException(String msg, Throwable e) {
        super(msg, e);
    }

    public GXSentinelFlowException(GXResultStatusCode resultCode) {
        super(resultCode);
    }

    public GXSentinelFlowException(GXResultStatusCode resultCode, String msg) {
        super(resultCode, msg);
    }

    public GXSentinelFlowException(GXResultStatusCode resultCode, Throwable e) {
        super(resultCode, e);
    }

    public GXSentinelFlowException(GXResultStatusCode resultCode, String msg, Dict data) {
        super(resultCode, msg, data);
    }

    public GXSentinelFlowException(GXResultStatusCode resultCode, Throwable e, Dict data) {
        super(resultCode, e, data);
    }

    public GXSentinelFlowException(GXResultStatusCode resultCode, String msg, Dict data, Throwable e) {
        super(resultCode, msg, data, e);
    }
}
