package cn.maple.extension.exception;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.exception.GXBusinessException;

/**
 * 扩展点初始化或者查找失败时，使用次异常
 * <p>
 * 扩展点初始化或者查找失败时，使用次异常
 * <p>
 */
public class GXExtensionException extends GXBusinessException {
    private static final String MSG = "扩展点异常";

    public GXExtensionException(String msg, int code, Dict data, Throwable e) {
        super(msg, code, data, e);
    }

    public GXExtensionException(String msg, int code, Dict data) {
        this(MSG, code, data, null);
    }

    public GXExtensionException(String msg, int code, Throwable e) {
        this(msg, code, Dict.create(), e);
    }

    public GXExtensionException(String msg, int code) {
        this(msg, code, Dict.create(), null);
    }

    public GXExtensionException(String msg, Throwable e) {
        this(msg, HttpStatus.HTTP_OK, e);
    }

    public GXExtensionException(String msg) {
        super(msg, HttpStatus.HTTP_OK, Dict.create(), null);
    }
}