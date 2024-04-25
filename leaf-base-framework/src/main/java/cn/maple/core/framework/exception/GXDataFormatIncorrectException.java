package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;

/**
 * 数据格式不正确
 * 例如 :
 * 1、手机格式不正确
 * 2、邮箱格式不正确
 */
public class GXDataFormatIncorrectException extends GXBusinessException {
    public GXDataFormatIncorrectException(String msg, int code, Dict data, Throwable e) {
        super(msg, code, data, e);
    }

    public GXDataFormatIncorrectException(String msg, int code, Dict data) {
        super(msg, code, data);
    }

    public GXDataFormatIncorrectException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXDataFormatIncorrectException(String msg, int code) {
        super(msg, code);
    }

    public GXDataFormatIncorrectException(String msg) {
        super(msg);
    }

    public GXDataFormatIncorrectException(String msg, Throwable e) {
        super(msg, e);
    }
}
