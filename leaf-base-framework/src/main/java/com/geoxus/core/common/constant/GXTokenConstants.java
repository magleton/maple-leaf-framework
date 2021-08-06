package com.geoxus.core.common.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class GXTokenConstants {
    @GXFieldCommentAnnotation(zhDesc = "TOKEN过期时间")
    public static final int EXPIRES = 24 * 60 * 60 * 15;

    @GXFieldCommentAnnotation(zhDesc = "USER端TOKEN即将过期刷新时间")
    public static final int USER_EXPIRES_REFRESH = 60 * 60 * 7;

    @GXFieldCommentAnnotation(zhDesc = "Admin登录用的token标签")
    public static final String ADMIN_ID = "admin_id";

    @GXFieldCommentAnnotation(zhDesc = "用户登录用的token标签")
    public static final String USER_ID = "user_id";

    @GXFieldCommentAnnotation(zhDesc = "管理员token的名字")
    public static final String ADMIN_TOKEN = "admin-token";

    @GXFieldCommentAnnotation(zhDesc = "用户token的名字")
    public static final String USER_TOKEN = "user-token";

    @GXFieldCommentAnnotation(zhDesc = "TOKEN加密的KEY")
    public static final String KEY = "GEO_XUS_SHIRO_TOKEN_KEY";

    @GXFieldCommentAnnotation(zhDesc = "ADMIN端TOKEN即将过期的刷新时间")
    public static final int ADMIN_EXPIRES_REFRESH = 24 * 60;

    private GXTokenConstants() {
    }
}
