package com.geoxus.core.common.exception;

import cn.hutool.core.lang.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GXBeanValidateException extends RuntimeException {
    
    private final String msg;

    private final int code;

    private final Dict dict;

    public GXBeanValidateException(String msg, int code, Dict dict) {
        super(msg);
        this.msg = msg;
        this.code = code;
        this.dict = dict;
    }
}
