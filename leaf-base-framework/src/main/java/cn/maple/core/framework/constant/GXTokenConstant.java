package cn.maple.core.framework.constant;

public class GXTokenConstant {
    /**
     * token的名字
     */
    public static final String TOKEN_NAME = "token";

    /**
     * TOKEN过期时间
     * 15天
     */
    public static final int USER_EXPIRE = 24 * 60 * 60 * 15;

    /**
     * USER端TOKEN即将过期刷新时间
     */
    public static final int USER_EXPIRE_REFRESH_INTERVAL = 24 * 60 * 60 * 7;

    /**
     * USER端缓存桶的名字(redisson)
     */
    public static final String USER_CACHE_BUCKET_NAME = "user-cache-bucket";

    /**
     * 用户token的名字
     */
    public static final String USER_TOKEN_NAME = "user-token";

    /**
     * 用户登录用的token标签
     */
    public static final String USER_ID = "userId";

    /**
     * Admin登录用的token标签
     */
    public static final String ADMIN_ID = "adminId";

    /**
     * 管理员token的名字
     */
    public static final String ADMIN_TOKEN_NAME = "admin-token";

    /**
     * ADMIN端缓存桶的名字(redisson)
     */
    public static final String ADMIN_CACHE_BUCKET_NAME = "admin-cache-bucket";

    /**
     * TOKEN加密的KEY
     */
    public static final String KEY = "GEO_XUS_SHIRO_TOKEN_KEY";

    /**
     * ADMIN端TOKEN即将过期刷新时间
     */
    public static final int ADMIN_EXPIRE_REFRESH_INTERVAL = 24 * 60;

    /**
     * 登录时间
     */
    public static final String LOGIN_AT_FIELD = "loginAt";

    /**
     * 平台
     */
    public static final String PLATFORM = "platform";

    private GXTokenConstant() {
    }
}
