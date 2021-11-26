package cn.maple.core.framework.code;

public enum GXResultCode {
    OK(200, "操作成功"),
    SMS_SEND_SUCCESS(200, "短信发送成功"),
    SMS_SEND_FAILURE(200, "短信发送失败"),
    COMMON_ERROR(1000, "系统错误"),
    INPUT_TOO_SHORT(1100, "输入为空,或者输入字数不够!"),
    DATA_NOT_FOUND(1200, "数据对象不存在"),
    SMS_CAPTCHA_ERROR(1300, "短信验证码错误"),
    GRAPH_CAPTCHA_ERROR(1400, "图形验证码错误"),
    WRONG_PHONE(1500, "手机号有误"),
    NEED_PERMISSION(1600, "权限不足"),
    LOGIN_ERROR(1700, "登录错误,账号或者密码错误!"),
    STATUS_ERROR(1800, "当前状态不正确"),
    NEED_GRAPH_CAPTCHA(1900, "请传递图形验证码"),
    USER_NAME_EXIST(2000, "用户名已存在"),
    PARAMETER_VALIDATION_ERROR(2100, "参数验证错误"),
    TOKEN_TIMEOUT_EXIT(2200, "登录信息已过期,请重新登录"),
    FILE_ERROR(2300, "不正确的文件格式"),
    PARSE_REQUEST_JSON_ERROR(2400, "解析请求的JSON参数出错"),
    REQUEST_JSON_NOT_BODY(2500, "请求的JSON参数出错为空"),
    ACCOUNT_FREEZE(2600, "账号被冻结"),
    USER_NOT_EXISTS(2700, "用户不存在");

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
