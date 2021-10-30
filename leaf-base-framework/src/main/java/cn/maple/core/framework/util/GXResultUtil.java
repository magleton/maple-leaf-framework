package cn.maple.core.framework.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.pojo.GXResultCode;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Objects;

@Data
public class GXResultUtil<T> {
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
    private T data = null;

    public static <T> GXResultUtil<T> ok(GXResultCode resultCode) {
        return ok(resultCode.getCode(), resultCode.getMsg());
    }

    public static <T> GXResultUtil<T> ok(GXResultCode resultCode, T data) {
        callUserDefinedMethod(data);
        return ok(resultCode.getCode(), resultCode.getMsg(), data);
    }

    public static <T> GXResultUtil<T> ok(String msg) {
        return ok(SUCCESS_CODE, msg, null);
    }

    public static <T> GXResultUtil<T> ok(int code, String msg) {
        return ok(code, msg, null);
    }

    public static <T> GXResultUtil<T> ok(int code) {
        return ok(code, SUCCESS_MSG, null);
    }

    public static <T> GXResultUtil<T> ok(T data) {
        callUserDefinedMethod(data);
        return ok(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> GXResultUtil<T> ok(String msg, T data) {
        callUserDefinedMethod(data);
        return ok(SUCCESS_CODE, msg, data);
    }

    public static <T> GXResultUtil<T> ok(int code, String msg, T data) {
        callUserDefinedMethod(data);
        GXResultUtil<T> r = new GXResultUtil<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    private static <T> void callUserDefinedMethod(T data) {
        if (Objects.nonNull(data)) {
            Class<?> aClass = TypeUtil.getClass(data.getClass());
            Method method = ReflectUtil.getMethodByName(aClass, "processResValue");
            if (Objects.nonNull(method)) {
                ReflectUtil.invoke(data, method);
            }
        }
    }

    public static <T> GXResultUtil<T> ok() {
        return new GXResultUtil<>();
    }

    public static <T> GXResultUtil<T> error() {
        return error(FAIL_CODE, "未知异常，请联系管理员");
    }

    public static <T> GXResultUtil<T> error(T data) {
        callUserDefinedMethod(data);
        return error(FAIL_CODE, FAIL_MSG, data);
    }

    public static <T> GXResultUtil<T> error(String msg) {
        return error(FAIL_CODE, msg);
    }

    public static <T> GXResultUtil<T> error(int code) {
        return error(code, FAIL_MSG, null);
    }

    public static <T> GXResultUtil<T> error(int code, String msg) {
        return error(code, msg, null);
    }

    public static <T> GXResultUtil<T> error(GXResultCode resultCode) {
        return error(resultCode.getCode(), resultCode.getMsg());
    }

    public static <T> GXResultUtil<T> error(GXResultCode resultCode, T data) {
        callUserDefinedMethod(data);
        return error(resultCode.getCode(), resultCode.getMsg(), data);
    }

    public static <T> GXResultUtil<T> error(int code, String msg, T data) {
        callUserDefinedMethod(data);
        GXResultUtil<T> r = new GXResultUtil<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
