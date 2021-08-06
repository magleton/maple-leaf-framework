package com.geoxus.core.common.vo.common;

public enum GXResultCode {
    OK(0, "操作成功"),
    NEED_PERMISSION(1000, "需要权限"),
    INPUT_TOO_SHORT(1001, "输入为空，或者输入字数不够"),
    TARGET_NOT_FOUND(1002, "相关的对象不存在"),
    SMS_CAPTCHA_ERROR(1003, "短信验证码错误"),
    GRAPH_CAPTCHA_ERROR(1004, "图形验证码错误"),
    WRONG_PHONE(1005, "手机号有误"),
    COMMON_ERROR(1006, "系统错误"),
    LOGIN_ERROR(1007, "登录错误"),
    SMS_SEND_SUCCESS(1008, "短信发送成功"),
    SMS_SEND_FAILURE(1009, "短信发送失败"),
    USER_NAME_EXIST(1010, "用户名已存在"),
    PARAMETER_VALIDATION_ERROR(1011, "参数验证错误"),
    TOKEN_TIMEOUT_EXIT(1012, "您已长时间未进行操作, 请重新登录~~~"),
    FILE_ERROR(1013, "不正确的文件"),
    PARSE_REQUEST_JSON_ERROR(1014, "解析请求的JSON参数出错"),
    REQUEST_JSON_NOT_BODY(1015, "请求的JSON参数出错为空"),
    FREEZE(1016, "账号被冻结"),
    PASSWORD_ERROR(1017, "账号或密码错误"),
    USER_NOT_EXISTS(1018, "用户不存在"),
    STATUS_ERROR(1019, "当前状态不正确"),
    NEED_GRAPH_CAPTCHA(1020, "请传递图形验证码");

    private final String msg;

    private final Integer code;

    GXResultCode(Integer code, String desc) {
        this.code = code;
        this.msg = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
