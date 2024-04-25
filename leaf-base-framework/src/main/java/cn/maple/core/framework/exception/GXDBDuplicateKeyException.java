package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.code.GXResultStatusCode;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import org.yaml.snakeyaml.error.Mark;

/**
 * @author britton@126.com
 * @since 2021-10-19 15:00
 * <p>
 * 数据库主键重复异常
 */
public class GXDBDuplicateKeyException extends GXBusinessException {

    public GXDBDuplicateKeyException(String msg, int code, Dict data, Throwable e) {
        super(msg, code, data, e);
    }

    public GXDBDuplicateKeyException(String msg, int code, Dict data) {
        super(msg, code, data);
    }

    public GXDBDuplicateKeyException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXDBDuplicateKeyException(String msg, int code) {
        super(msg, code);
    }

    public GXDBDuplicateKeyException(String msg) {
        super(msg);
    }

    public GXDBDuplicateKeyException(String msg, Throwable e) {
        super(msg, e);
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode) {
        super(resultCode);
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode, String msg) {
        super(resultCode, msg);
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode, Throwable e) {
        super(resultCode, e);
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode, String msg, Dict data) {
        super(resultCode, msg, data);
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode, Throwable e, Dict data) {
        super(resultCode, e, data);
    }

    public GXDBDuplicateKeyException(GXResultStatusCode resultCode, String msg, Dict data, Throwable e) {
        super(resultCode, msg, data, e);
    }
}
