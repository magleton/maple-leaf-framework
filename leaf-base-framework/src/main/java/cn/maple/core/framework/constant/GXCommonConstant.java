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
    public static final String COMMON_ENCRYPT_KEY = "B78D32BTR1CHEN15AC1F19C46A9B533986";

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
    public static final String FULL_LOGGER_FORMAT = "线程 : {} --> 描述信息 : {} --> 日志详细 : {}";

    /**
     * 日志格式
     */
    public static final String SHORT_LOGGER_FORMAT = "线程 : {} --> 描述信息 : {}";

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

    /**
     * 服务敏感信息的加解密KEY的名字,需要通过
     * {@code -Dmaple.ds.secret=britton}
     * 虚拟机参数指定
     * 1、数据库
     * 2、redis
     * 3、MongoDB
     */
    public static final String DATA_SOURCE_SECRET_KEY = "maple.ds.secret";

    /**
     * 系统(环境)变量里面配置的服务敏感信息的加解密KEY的名字
     * {@code
     * Linux : export JAVA_BIZ_SECRET_KEY = "aaa-bbb";
     * Windows : 在环境变量里面设置
     * }
     */
    public static final String DATA_SOURCE_SECRET_KEY_ENV = "JAVA_BIZ_SECRET_KEY";

    private GXCommonConstant() {
    }
}
