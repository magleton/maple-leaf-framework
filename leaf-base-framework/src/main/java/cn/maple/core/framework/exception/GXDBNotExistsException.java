package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.code.GXHttpStatusCode;

public class GXDBNotExistsException extends RuntimeException {
    private final String msg;

    private final int code;

    private Dict data;

    public GXDBNotExistsException(String msg, int code, Dict data, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public GXDBNotExistsException(String msg, int code, Dict data) {
        this(msg, code, data, null);
    }

    public GXDBNotExistsException(String msg, int code, Throwable e) {
        this(msg, code, Dict.create(), e);
    }

    public GXDBNotExistsException(String msg, int code) {
        this(msg, code, Dict.create());
    }

    public GXDBNotExistsException(String msg) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR);
    }

    public GXDBNotExistsException(String msg, Throwable e) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR, e);
    }

    public GXDBNotExistsException(GXHttpStatusCode resultCode) {
        this(resultCode, null);
    }

    public GXDBNotExistsException(GXHttpStatusCode resultCode, Throwable e) {
        super(resultCode.getMsg(), e);
        this.msg = resultCode.getMsg();
        this.code = resultCode.getCode();
    }
}
