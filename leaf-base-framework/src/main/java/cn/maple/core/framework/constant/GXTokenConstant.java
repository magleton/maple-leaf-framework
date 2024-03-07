package cn.maple.core.framework.constant;

public class GXTokenConstant {
    /**
     * token的名字
     */
    public static final String TOKEN_NAME = "token";

    /**
     * Token永不过期
     */
    public static final Integer TOKEN_NEVER_EXPIRE = 0;

    /**
     * TOKEN过期时间
     * 15天
     */
    public static final int USER_EXPIRE = 24 * 60 * 60 * 15;

    /**
     * USER端TOKEN即将过期刷新阈值
     * 当用户的token过期时间小于该值时
     * 需要刷新缓存中的token过期时间
     */
    public static final int USER_EXPIRE_REFRESH_THRESHOLD = 24 * 60 * 60 * 3;

    /**
     * 用户端的token续期时间
     * 当用户的token小于USER_EXPIRE_REFRESH_THRESHOLD时
     * 需要将用户的token过期时间设置为 currentDate + USER_TOKEN_RENEWAL
     */
    public static final int USER_TOKEN_RENEWAL = 24 * 60 * 60 * 7;

    /**
     * USER端缓存桶的名字(redisson)
     */
    public static final String USER_CACHE_BUCKET_NAME = "user-cache-bucket";

    /**
     * 用户token的名字
     */
    public static final String USER_TOKEN_NAME = "token";

    /**
     * 用户登录用的token标签
     */
    public static final String TOKEN_USER_ID_FIELD_NAME = "userId";

    /**
     * 用户登录用的token标签
     */
    public static final String TOKEN_USER_NAME_FIELD_NAME = "userName";

    /**
     * 管理端缓存桶的名字(redisson)
     */
    public static final String MANAGER_CACHE_BUCKET_NAME = "admin-cache-bucket";

    /**
     * C端客户TOKEN加密的KEY
     */
    public static final String USER_TOKEN_SECRET_KEY = "6A3EDD4768E3669B5";

    /**
     * 管理端TOKEN加密的KEY
     */
    public static final String ADMIN_TOKEN_SECRET_KEY = "C0180A90D3931D";

    /**
     * 通用的TOKEN加密的KEY
     */
    public static final String TOKEN_SECRET_KEY = "31D0D393D3EAF5";

    /**
     * ADMIN端TOKEN即将过期刷新阈值
     * 当admin的token过期时间小于该值时
     * 需要刷新缓存中的token过期时间
     */
    public static final int ADMIN_EXPIRE_REFRESH_THRESHOLD = 24 * 60;

    /**
     * 管理端的token续期时间
     * 当管理员的token小于ADMIN_EXPIRE_REFRESH_THRESHOLD时
     * 需要将管理员的token过期时间设置为 currentDate + ADMIN_TOKEN_RENEWAL
     */
    public static final int ADMIN_TOKEN_RENEWAL = 12 * 60 * 60;

    /**
     * 登录时间
     */
    public static final String LOGIN_AT_FIELD_NAME = "loginAt";

    /**
     * 平台
     */
    public static final String PLATFORM = "platform";

    private GXTokenConstant() {
    }
}
