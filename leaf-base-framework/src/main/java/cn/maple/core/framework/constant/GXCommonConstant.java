package cn.maple.core.framework.constant;

import cn.maple.core.framework.annotation.GXFieldComment;

public class GXCommonConstant {
    @GXFieldComment("默认当前分页")
    public static final int DEFAULT_CURRENT_PAGE = 1;

    @GXFieldComment("默认每页的大小")
    public static final int DEFAULT_PAGE_SIZE = 20;

    @GXFieldComment("默认分页的最大值")
    public static final int DEFAULT_MAX_PAGE_SIZE = 10000;

    @GXFieldComment("验证数字的正则表达式")
    public static final String DIGITAL_REGULAR_EXPRESSION = "^[+-]?(0|([1-9]\\d*))(\\.\\d+)?$";

    @GXFieldComment("手机号码加密的KEY")
    public static final String PHONE_ENCRYPT_KEY = "B78D32BTR1CHEN15AC1F19C46A9B533986";

    @GXFieldComment("未删除的标识")
    public static final Object NOT_DELETED_MARK = 0;

    /**
     * 日志格式
     */
    public static final String LOGGER_FORMAT = "{} : {} ---> {}";

    private GXCommonConstant() {
    }
}
