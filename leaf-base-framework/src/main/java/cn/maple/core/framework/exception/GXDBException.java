package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.code.GXResultStatusCode;

/**
 * @author britton@126.com
 * @since 2021-10-19 15:06
 * <p>
 * 数据库通用异常
 */
public class GXDBException extends GXBusinessException {
    public GXDBException(String msg, int code, Dict data, Throwable e) {
        super(msg, code, data, e);
    }

    public GXDBException(String msg, int code, Dict data) {
        super(msg, code, data);
    }

    public GXDBException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXDBException(String msg, int code) {
        super(msg, code);
    }

    public GXDBException(String msg) {
        super(msg);
    }

    public GXDBException(String msg, Throwable e) {
        super(msg, e);
    }

    public GXDBException(GXResultStatusCode resultCode) {
        super(resultCode);
    }

    public GXDBException(GXResultStatusCode resultCode, String msg) {
        super(resultCode, msg);
    }

    public GXDBException(GXResultStatusCode resultCode, Throwable e) {
        super(resultCode, e);
    }

    public GXDBException(GXResultStatusCode resultCode, String msg, Dict data) {
        super(resultCode, msg, data);
    }

    public GXDBException(GXResultStatusCode resultCode, Throwable e, Dict data) {
        super(resultCode, e, data);
    }

    public GXDBException(GXResultStatusCode resultCode, String msg, Dict data, Throwable e) {
        super(resultCode, msg, data, e);
    }
}
