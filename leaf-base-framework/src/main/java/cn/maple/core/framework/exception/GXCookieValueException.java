package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.code.GXResultStatusCode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GXCookieValueException extends RuntimeException {
    private final String msg;

    private final int code;

    private final Dict data;

    public GXCookieValueException(String msg, int code, Dict data, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public GXCookieValueException(String msg, int code, Dict data) {
        this(msg, code, data, null);
    }

    public GXCookieValueException(String msg, int code, Throwable e) {
        this(msg, code, Dict.create(), e);
    }

    public GXCookieValueException(String msg, int code) {
        this(msg, code, (Dict) null);
    }

    public GXCookieValueException(String msg) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR);
    }

    public GXCookieValueException(String msg, Throwable e) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR, e);
    }

    public GXCookieValueException(GXResultStatusCode resultCode) {
        this(resultCode, "");
    }

    public GXCookieValueException(GXResultStatusCode resultCode, @NotNull String msg) {
        this(resultCode, msg, Dict.create());
    }

    public GXCookieValueException(GXResultStatusCode resultCode, Throwable e) {
        this(resultCode, e, Dict.create());
    }

    public GXCookieValueException(GXResultStatusCode resultCode, @NotNull String msg, @NotNull Dict data) {
        this(resultCode, msg, data, null);
    }

    public GXCookieValueException(GXResultStatusCode resultCode, Throwable e, @NotNull Dict data) {
        this(resultCode, "", data, e);
    }

    public GXCookieValueException(GXResultStatusCode resultCode, @NotNull String msg, @NotNull Dict data, Throwable e) {
        super(CharSequenceUtil.format("{}{}", msg, resultCode.getMsg()), e);
        this.msg = CharSequenceUtil.format("{}{}", msg, resultCode.getMsg());
        this.code = resultCode.getCode();
        if (MapUtil.isEmpty(data)) {
            data = Dict.create();
        }
        data.putAll(resultCode.getExtraData());
        this.data = data;
    }
}
