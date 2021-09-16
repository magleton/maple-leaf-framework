package com.geoxus.sso.constant;

import java.nio.charset.Charset;

/**
 * <p>
 * SSO 定义常量
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public class GXSsoConstant {
    /**
     * 编码
     */
    public static final String ENCODING = "UTF-8";

    /**
     * Token中的IP字段名字
     */
    public static final String TOKEN_USER_IP = "ip";

    /**
     * token中的USER_AGENT字段名字
     */
    public static final String TOKEN_USER_AGENT = "ua";

    /**
     * token中的flag字段名字
     */
    public static final String TOKEN_FLAG = "fg";

    /**
     * token中的ORIGIN字段名字
     */
    public static final String TOKEN_ORIGIN = "og";

    /**
     * token中的租户ID名字
     */
    public static final String TOKEN_TENANT_ID = "tid";

    /**
     * token使用的算法
     */
    public static final String RSA = "RSA";

    /**
     * token的名字
     */
    public static final String ACCESS_SECRET = "accessSecret";

    /**
     * 拦截器判断后设置 Token至当前请求<br>
     * 减少Token解密次数： request.setAttribute("sso_token_attr", token)
     * <p>
     * 使用获取方式： GXSsoHelper.attrToken(request)
     * </p>
     */
    public static final String SSO_TOKEN_ATTR = "sso_token_attr";

    /**
     * 踢出用户逻辑标记
     */
    public static final String SSO_KICK_FLAG = "sso_kick_flag";

    public static final String SSO_KICK_USER = "sso_kick_user";

    /**
     * SSO 动态设置 Cookie 参数
     * <p>
     * -1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
     * </p>
     */
    public static final String SSO_COOKIE_MAX_AGE = "sso_cookie_max_age";

    /**
     * Charset 类型编码格式
     */
    public static final Charset CHARSET_ENCODING = Charset.forName(ENCODING);

    /**
     * 分隔符
     */
    public static final String CUT_SYMBOL = "#";

    /**
     * 时间戳的截断位所用除数
     * <p>
     * 用于token中时间戳(例如iat,exp) 和 系统内System.currentTimeMillis()时间戳的值的比对
     * <p>
     * 前者精确到秒级别,后者精确到毫秒级,相差3位
     */
    public static final Long TOKEN_TIMESTAMP_CUT = 1000L;

    private GXSsoConstant() {
    }
}