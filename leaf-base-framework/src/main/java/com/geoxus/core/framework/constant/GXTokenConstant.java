package com.geoxus.core.framework.constant;

import com.geoxus.core.framework.annotation.GXFieldComment;

public class GXTokenConstant {
    @GXFieldComment(zhDesc = "TOKEN过期时间")
    public static final int EXPIRES = 24 * 60 * 60 * 15;

    @GXFieldComment(zhDesc = "USER端TOKEN即将过期刷新时间")
    public static final int USER_EXPIRES_REFRESH = 60 * 60 * 7;

    @GXFieldComment(zhDesc = "Admin登录用的token标签")
    public static final String ADMIN_ID = "admin_id";

    @GXFieldComment(zhDesc = "用户登录用的token标签")
    public static final String USER_ID = "user_id";

    @GXFieldComment(zhDesc = "管理员token的名字")
    public static final String ADMIN_TOKEN = "admin-token";

    @GXFieldComment(zhDesc = "用户token的名字")
    public static final String USER_TOKEN = "user-token";

    @GXFieldComment(zhDesc = "TOKEN加密的KEY")
    public static final String KEY = "GEO_XUS_SHIRO_TOKEN_KEY";

    @GXFieldComment(zhDesc = "ADMIN端TOKEN即将过期的刷新时间")
    public static final int ADMIN_EXPIRES_REFRESH = 24 * 60;

    private GXTokenConstant() {
    }
}
