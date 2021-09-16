package com.geoxus.sso.util;

import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.sso.cache.GXSsoCache;
import com.geoxus.sso.config.GXSsoConfig;
import com.geoxus.sso.plugins.impl.GXLoginSsoPlugin;
import com.geoxus.sso.security.token.GXSsoToken;
import com.geoxus.sso.service.GXConfigurableAbstractSsoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * <p>
 * SSO 帮助类
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public class GXSsoHelperUtil {
    protected static GXSsoConfig ssoConfig;

    protected static GXConfigurableAbstractSsoService ssoService;

    private GXSsoHelperUtil() {

    }

    public static GXSsoConfig getSsoConfig() {
        if (null == ssoConfig) {
            ssoConfig = new GXSsoConfig();
            ssoConfig.setCache(GXSpringContextUtils.getBean(GXSsoCache.class));
            ssoConfig.setPluginList(Collections.singletonList(new GXLoginSsoPlugin()));
        }
        return ssoConfig;
    }

    public static GXSsoConfig setSsoConfig(GXSsoConfig ssoConfig) {
        GXSsoHelperUtil.ssoConfig = ssoConfig;
        return GXSsoHelperUtil.ssoConfig;
    }

    /**
     * Sso 服务初始化
     */
    public static GXConfigurableAbstractSsoService getSsoService() {
        if (ssoService == null) {
            ssoService = new GXConfigurableAbstractSsoService();
        }
        return ssoService;
    }

    /**
     * HS512 密钥
     */
    public static String getHS512SecretKey() {
        return "密钥";/*JwtHelper.getHS512SecretKey()*/
    }

    // ------------------------------- 登录相关方法 -------------------------------

    /**
     * 设置加密 Cookie（登录验证成功）<br>
     * 最后一个参数 true 销毁当前JSESSIONID. 创建可信的 JSESSIONID 防止伪造 SESSIONID 攻击
     * <p>
     * 最后一个参数 false 只设置 cookie
     * <p>
     * request.setAttribute(GXSsoConfig.SSO_COOKIE_MAX_AGE, maxAge);<br>
     * 可以动态设置 Cookie maxAge 超时时间 ，优先于配置文件的设置，无该参数 - 默认读取配置文件数据 。<br>
     * maxAge 定义：-1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
     *
     * @param request    请求对象
     * @param response   响应对象
     * @param ssoToken   SSO 票据
     * @param invalidate 销毁当前 JSESSIONID
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, GXSsoToken ssoToken, boolean invalidate) {
        if (invalidate) {
            getSsoService().authCookie(request, response, ssoToken);
        } else {
            getSsoService().setCookie(request, response, ssoToken);
        }
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, GXSsoToken ssoToken) {
        setCookie(request, response, ssoToken, false);
    }

    // ------------------------------- 客户端相关方法 -------------------------------

    /**
     * <p>
     * 获取当前请求 token<br>
     * 该方法直接从 cookie 中解密获取 token, 常使用在登录系统及拦截器中使用 getToken(request)
     * </p>
     * <p>
     * 如果该请求在登录拦截器之后请使用 attrToken(request) 防止二次解密
     * </p>
     *
     * @param request 请求对象
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T extends GXSsoToken> T getSsoToken(HttpServletRequest request) {
        return (T) getSsoService().getSsoToken(request);
    }

    /**
     * <p>
     * 从请求中获取 token 通过登录拦截器之后使用<br>
     * 该数据为登录拦截器放入 request 中，防止二次解密
     * </p>
     *
     * @param request 访问请求
     * @return T
     */
    public static <T extends GXSsoToken> T attrToken(HttpServletRequest request) {
        return getSsoService().attrSsoToken(request);
    }

    /**
     * <p>
     * 退出登录， 并且跳至 sso.properties 配置的属性 sso.logout.url 地址
     * </p>
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws IOException
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getSsoService().logout(request, response);
    }

    /**
     * 清理当前登录状态<br>
     * 清理 Cookie、缓存、统计、等数据
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return boolean
     */
    public static boolean clearLogin(HttpServletRequest request, HttpServletResponse response) {
        return getSsoService().clearLogin(request, response);
    }

    /**
     * 退出重定向登录页，跳至 sso.properties 配置的属性 sso.login.url 地址
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws IOException
     */
    public static void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getSsoService().clearRedirectLogin(request, response);
    }

    /**
     * 获取 token 的缓存主键
     *
     * @param request 当前请求
     * @return String
     */
    public static String getTokenCacheKey(HttpServletRequest request) {
        return getSsoToken(request).toCacheKey();
    }

    /**
     * 获取 token 的缓存主键
     *
     * @param userId 用户ID
     * @return String
     */
    public static String getTokenCacheKey(Object userId) {
        return GXSsoConfig.toCacheKey(userId);
    }

    /**
     * 踢出 指定用户 ID 的登录用户，退出当前系统。
     *
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean kickLogin(Object userId) {
        return getSsoService().kickLogin(userId);
    }
}