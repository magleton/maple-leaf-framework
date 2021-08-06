package com.geoxus.core.common.util;

import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.geoxus.core.common.vo.common.GXResultCode;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GXResultUtils<T> {
    private static final long serialVersionUID = 1L;

    /**
     * 失败的提示消息
     */
    private static final String FAIL_MSG = "fail";

    /**
     * 成功的提示消息
     */
    private static final String SUCCESS_MSG = "success";

    /**
     * 成功的CODE
     */
    private static final int SUCCESS_CODE = HttpStatus.HTTP_OK;

    /**
     * 失败的CODE
     */
    private static final int FAIL_CODE = HttpStatus.HTTP_INTERNAL_ERROR;

    /**
     * 返回code码，200成功，其他失败
     */
    private int code = SUCCESS_CODE;

    /**
     * 错误信息
     */
    private String msg = SUCCESS_MSG;

    /**
     * 返回数据
     */
    private T data;

    public static <T> GXResultUtils<T> ok(GXResultCode resultCode) {
        return ok(resultCode.getCode(), resultCode.getMsg());
    }

    public static <T> GXResultUtils<T> ok(GXResultCode resultCode, T data) {
        return ok(resultCode.getCode(), resultCode.getMsg(), data);
    }

    public static <T> GXResultUtils<T> ok(String msg) {
        return ok(SUCCESS_CODE, msg, null);
    }

    public static <T> GXResultUtils<T> ok(int code, String msg) {
        return ok(code, msg, null);
    }

    public static <T> GXResultUtils<T> ok(int code) {
        return ok(code, SUCCESS_MSG, null);
    }

    public static <T> GXResultUtils<T> ok(T data) {
        return ok(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> GXResultUtils<T> ok(int code, String msg, T data) {
        GXResultUtils<T> r = new GXResultUtils<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static <T> GXResultUtils<T> ok() {
        return new GXResultUtils<>();
    }

    public static <T> GXResultUtils<T> error() {
        return error(FAIL_CODE, "未知异常，请联系管理员");
    }

    public static <T> GXResultUtils<T> error(T data) {
        return error(FAIL_CODE, FAIL_MSG, data);
    }

    public static <T> GXResultUtils<T> error(String msg) {
        return error(FAIL_CODE, msg);
    }

    public static <T> GXResultUtils<T> error(int code) {
        return error(code, FAIL_MSG, null);
    }

    public static <T> GXResultUtils<T> error(int code, String msg) {
        return error(code, msg, null);
    }

    public static <T> GXResultUtils<T> error(GXResultCode resultCode) {
        return error(resultCode.getCode(), resultCode.getMsg());
    }

    public static <T> GXResultUtils<T> error(GXResultCode resultCode, T data) {
        return error(resultCode.getCode(), resultCode.getMsg(), data);
    }

    public static <T> GXResultUtils<T> error(int code, String msg, T data) {
        GXResultUtils<T> r = new GXResultUtils<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
