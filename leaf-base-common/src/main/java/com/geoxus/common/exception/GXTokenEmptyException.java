package com.geoxus.common.exception;

import com.geoxus.common.exception.GXBusinessException;
import com.geoxus.common.pojo.GXResultCode;

public class GXTokenEmptyException extends GXBusinessException {
    public GXTokenEmptyException(String msg) {
        super(msg);
    }

    public GXTokenEmptyException(String msg, Throwable e) {
        super(msg, e);
    }

    public GXTokenEmptyException(String msg, int code) {
        super(msg, code);
    }

    public GXTokenEmptyException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public GXTokenEmptyException(GXResultCode resultCode) {
        super(resultCode);
    }

    public GXTokenEmptyException(GXResultCode resultCode, Throwable e) {
        super(resultCode, e);
    }
}
