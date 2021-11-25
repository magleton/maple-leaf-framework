package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.code.GXResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 *
 * @author zj chen <britton@126.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GXBusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String msg;

    private final int code;

    private Dict data;

    public GXBusinessException(String msg, int code, Dict data, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public GXBusinessException(String msg, int code, Dict data) {
        this(msg, code, data, null);
    }

    public GXBusinessException(String msg, int code, Throwable e) {
        this(msg, code, Dict.create(), e);
    }

    public GXBusinessException(String msg, int code) {
        this(msg, code, (Dict) null);
    }

    public GXBusinessException(String msg) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR);
    }

    public GXBusinessException(String msg, Throwable e) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR, e);
    }

    public GXBusinessException(GXResultCode resultCode) {
        this(resultCode, null);
    }

    public GXBusinessException(GXResultCode resultCode, Throwable e) {
        super(resultCode.getMsg(), e);
        this.msg = resultCode.getMsg();
        this.code = resultCode.getCode();
    }
}
