package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.pojo.GXResultCode;

public class GXDBConditionException extends RuntimeException {
    private final String msg;

    private final int code;

    private Dict data;

    public GXDBConditionException(String msg, int code, Dict data, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public GXDBConditionException(String msg, int code, Dict data) {
        this(msg, code, data, null);
    }

    public GXDBConditionException(String msg, int code, Throwable e) {
        this(msg, code, Dict.create(), e);
    }

    public GXDBConditionException(String msg, int code) {
        this(msg, code, Dict.create());
    }

    public GXDBConditionException(String msg) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR);
    }

    public GXDBConditionException(String msg, Throwable e) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR, e);
    }

    public GXDBConditionException(GXResultCode resultCode) {
        this(resultCode, null);
    }

    public GXDBConditionException(GXResultCode resultCode, Throwable e) {
        super(resultCode.getMsg(), e);
        this.msg = resultCode.getMsg();
        this.code = resultCode.getCode();
    }
}
