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
public class GXCorsConfigException extends RuntimeException {
    private final String msg;

    private final int code;

    private final Dict data;

    public GXCorsConfigException(String msg, int code, Dict data, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public GXCorsConfigException(String msg, int code, Dict data) {
        this(msg, code, data, null);
    }

    public GXCorsConfigException(String msg, int code, Throwable e) {
        this(msg, code, Dict.create(), e);
    }

    public GXCorsConfigException(String msg, int code) {
        this(msg, code, (Dict) null);
    }

    public GXCorsConfigException(String msg) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR);
    }

    public GXCorsConfigException(String msg, Throwable e) {
        this(msg, HttpStatus.HTTP_INTERNAL_ERROR, e);
    }

    public GXCorsConfigException(GXResultStatusCode resultCode) {
        this(resultCode, "");
    }

    public GXCorsConfigException(GXResultStatusCode resultCode, @NotNull String msg) {
        this(resultCode, msg, Dict.create());
    }

    public GXCorsConfigException(GXResultStatusCode resultCode, Throwable e) {
        this(resultCode, e, Dict.create());
    }

    public GXCorsConfigException(GXResultStatusCode resultCode, @NotNull String msg, @NotNull Dict data) {
        this(resultCode, msg, data, null);
    }

    public GXCorsConfigException(GXResultStatusCode resultCode, Throwable e, @NotNull Dict data) {
        this(resultCode, "", data, e);
    }

    public GXCorsConfigException(GXResultStatusCode resultCode, @NotNull String msg, @NotNull Dict data, Throwable e) {
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
