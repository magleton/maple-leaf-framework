package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXSentinelFlowException extends Exception {
    /**
     * 异常消息
     */
    private final String msg;

    /**
     * 异常码
     */
    private final int code;

    /**
     * 异常数据
     */
    private final Dict data;

    public GXSentinelFlowException(String msg, int code, Dict data, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public GXSentinelFlowException(String msg, int code, Dict data) {
        this(msg, code, data, null);
    }

    public GXSentinelFlowException(String msg, int code, Throwable e) {
        this(msg, code, null, e);
    }

    public GXSentinelFlowException(String msg, int code) {
        this(msg, code, null, null);
    }

    public GXSentinelFlowException(String msg) {
        this(msg, HttpStatus.HTTP_NOT_ACCEPTABLE, null, null);
    }

    public GXSentinelFlowException(String msg, Dict data) {
        this(msg, HttpStatus.HTTP_NOT_ACCEPTABLE, data, null);
    }
}
