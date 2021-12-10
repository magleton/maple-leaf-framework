package cn.maple.core.framework.constant;

public class GXCommonConstant {
    /**
     * 默认当前分页
     */
    public static final int DEFAULT_CURRENT_PAGE = 1;

    /**
     * 默认每页的大小
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 默认分页的最大值
     */
    public static final int DEFAULT_MAX_PAGE_SIZE = 10000;

    /**
     * 验证数字的正则表达式
     */
    public static final String DIGITAL_REGULAR_EXPRESSION = "^[+-]?(0|([1-9]\\d*))(\\.\\d+)?$";

    /**
     * 手机号码加密的KEY
     */
    public static final String PHONE_ENCRYPT_KEY = "B78D32BTR1CHEN15AC1F19C46A9B533986";

    /**
     * 未删除的标识值,整形类型
     */
    public static final Integer NOT_INT_DELETED_MARK = 0;

    /**
     * 未删除的标识值,字符串类型
     */
    public static final String NOT_STR_DELETED_MARK = "'0'";

    /**
     * 日志格式
     */
    public static final String LOGGER_FORMAT = "线程 : {} --> 描述信息 : {} --> 详细信息 : {}";

    /**
     * 服务运行的环境
     * 个人开发环境
     */
    public static final String RUN_ENV_LOCAL = "dev";

    /**
     * 服务运行的环境
     * 开发模式
     */
    public static final String RUN_ENV_DEV = "dev";

    /**
     * 服务运行的环境
     * 测试模式
     */
    public static final String RUN_ENV_TEST = "test";

    /**
     * 服务运行的环境
     * 预发布模式
     */
    public static final String RUN_ENV_PRE = "pre";

    /**
     * 服务运行的环境
     * 生产模式
     */
    public static final String RUN_ENV_PROD = "prod";

    private GXCommonConstant() {
    }
}
