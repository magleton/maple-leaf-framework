package com.geoxus.core.common.exception;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import com.geoxus.core.common.vo.common.GXResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 *
 * @author zj chen <britton@126.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GXException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String msg;

    private final int code;

    private Dict data;

    public GXException(String msg, int code, Dict data, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public GXException(String msg, int code, Dict data) {
        this(msg, code, data, null);
    }

    public GXException(String msg, int code, Throwable e) {
        this(msg, code, Dict.create(), e);
    }

    public GXException(String msg, int code) {
        this(msg, code, Dict.create());
    }

    public GXException(String msg) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR);
    }

    public GXException(String msg, Throwable e) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR, e);
    }

    public GXException(GXResultCode resultCode) {
        this(resultCode, null);
    }

    public GXException(GXResultCode resultCode, Throwable e) {
        super(resultCode.getMsg(), e);
        this.msg = resultCode.getMsg();
        this.code = resultCode.getCode();
    }
}
