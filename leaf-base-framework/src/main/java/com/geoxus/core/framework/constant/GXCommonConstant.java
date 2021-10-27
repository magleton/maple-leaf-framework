package com.geoxus.core.framework.constant;

import com.geoxus.core.framework.annotation.GXFieldComment;

public class GXCommonConstant {
    @GXFieldComment(zhDesc = "手机验证码标识")
    public static final int SMS_VERIFY = 1;

    @GXFieldComment(zhDesc = "图形验证码标识")
    public static final int CAPTCHA_VERIFY = 2;

    @GXFieldComment(zhDesc = "电子邮箱验证码标识")
    public static final int EMAIL_VERIFY = 3;

    @GXFieldComment(zhDesc = "默认当前分页")
    public static final int DEFAULT_CURRENT_PAGE = 1;

    @GXFieldComment(zhDesc = "默认每页的大小")
    public static final int DEFAULT_PAGE_SIZE = 20;

    @GXFieldComment(zhDesc = "默认数据")
    public static final String DEFAULT_DATA = "控制器默认方法";

    @GXFieldComment(zhDesc = "验证数字的正则表达式")
    public static final String DIGITAL_REGULAR_EXPRESSION = "^[+-]?(0|([1-9]\\d*))(\\.\\d+)?$";

    @GXFieldComment(zhDesc = "手机号码加密的KEY")
    public static final String PHONE_ENCRYPT_KEY = "B78D32BTR1CHEN15AC1F19C46A9B533986";

    @GXFieldComment(zhDesc = "状态字段的名字")
    public static final String STATUS_FIELD_NAME = "status";

    @GXFieldComment("请求参数中的多媒体字段名字")
    public static final String MEDIA_FIELD_NAME = "media";

    @GXFieldComment("自定义搜索条件的字段名字")
    public static final String SEARCH_MIXED_FIELD_CONDITION = "mixed";

    @GXFieldComment("模型标识的字段名字")
    public static final String CORE_MODEL_PRIMARY_FIELD_NAME = "core_model_id";

    /**
     * 日志格式
     */
    public static final String LOGGER_FORMAT = "{} : {} ---> {}";

    private GXCommonConstant() {
    }
}
