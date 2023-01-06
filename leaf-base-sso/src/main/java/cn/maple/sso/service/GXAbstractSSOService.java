package cn.maple.sso.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.util.GXCurrentRequestContextUtils;
import cn.maple.sso.cache.GXSSOCache;
import cn.maple.sso.enums.GXTokenFlag;
import cn.maple.sso.plugins.GXSSOPlugin;
import cn.maple.sso.security.token.GXSSOToken;
import cn.maple.sso.utils.GXCookieHelperUtil;
import cn.maple.sso.utils.GXHttpUtil;
import cn.maple.sso.utils.GXRandomUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * SSO 单点登录服务抽象实现类
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
@Slf4j
public abstract class GXAbstractSSOService extends GXSSOSupportService implements GXSSOService {
    /**
     * 获取当前请求 GXSsoToken
     * 从 Cookie 解密 GXSsoToken 使用场景, 拦截器
     * 非拦截器建议使用 attrSSOToken 减少二次解密
     *
     * @param request 请求对象
     * @return GXSsoToken {@link GXSSOToken}
     */
    @Override
    public GXSSOToken getSSOToken(HttpServletRequest request) {
        GXSSOToken cacheSSOToken = cacheSSOToken(request, getConfig().getCache());
        GXSSOToken token = checkIpBrowser(request, cacheSSOToken);
        if (Objects.isNull(token)) {
            return null;
        }
        // 执行插件逻辑
        List<GXSSOPlugin> pluginList = getConfig().getPluginList();
        if (pluginList != null) {
            for (GXSSOPlugin plugin : pluginList) {
                boolean valid = plugin.validateToken(token);
                if (!valid) {
                    return null;
                }
            }
        }
        return token;
    }

    /**
     * 踢出 指定用户 ID 的登录用户，退出当前系统。=
     *
     * @param userId 用户ID
     * @return boolean
     */
    @Override
    public boolean kickLogin(Object userId) {
        GXSSOCache cache = getConfig().getCache();
        if (cache != null) {
            GXSSOToken ssoToken = getSSOToken(GXCurrentRequestContextUtils.getHttpServletRequest());
            return cache.delete(ssoToken);
        } else {
            log.debug(" kickLogin! please implements GXSsoCache class.");
        }
        return false;
    }

    /**
     * 当前访问域下设置登录Cookie
     * <p>
     * request.setAttribute(GXSsoConfig.SSO_COOKIE_MAX_AGE, -1);
     * 可以设置 Cookie 超时时间 ，默认读取配置文件数据 。
     * -1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
     *
     * @param request  请求对象
     * @param response 响应对象
     */
    @Override
    public void setCookie(HttpServletRequest request, HttpServletResponse response, GXSSOToken ssoToken) {
        // 设置加密 Cookie
        Cookie ck = this.generateCookie(request, ssoToken);

        // 判断 GXSsoCache 是否缓存处理失效
        // cache 缓存宕机，flag 设置为失效
        GXSSOCache cache = getConfig().getCache();
        if (cache != null) {
            GXSSOToken cookieSSOToken = getSSOTokenFromCookie(GXCurrentRequestContextUtils.getHttpServletRequest());
            boolean rlt = cache.set(ssoToken, getConfig().getCacheExpires(), cookieSSOToken);
            if (!rlt) {
                ssoToken.setFlag(GXTokenFlag.CACHE_SHUT);
            }
        }

        //执行插件逻辑
        List<GXSSOPlugin> pluginList = getConfig().getPluginList();
        if (pluginList != null) {
            for (GXSSOPlugin plugin : pluginList) {
                boolean login = plugin.login(request, response);
                if (!login) {
                    plugin.login(request, response);
                }
            }
        }

        // Cookie设置HttpOnly
        if (getConfig().isCookieHttpOnly()) {
            GXCookieHelperUtil.addHttpOnlyCookie(response, ck);
        } else {
            response.addCookie(ck);
        }
    }

    /**
     * 当前访问域下设置登录Cookie 设置防止伪造SESSION_ID攻击
     *
     * @param request  请求对象
     * @param response 响应对象
     */
    public void authCookie(HttpServletRequest request, HttpServletResponse response, GXSSOToken ssoToken) {
        GXCookieHelperUtil.authJSESSIONID(request, GXRandomUtil.getCharacterAndNumber(8));
        this.setCookie(request, response, ssoToken);
    }

    /**
     * 清除登录状态
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return boolean true 成功, false 失败
     */
    @Override
    public boolean clearLogin(HttpServletRequest request, HttpServletResponse response) {
        return logout(request, response, getConfig().getCache());
    }

    /**
     * <p>
     * 重新登录 退出当前登录状态、重定向至登录页.
     * </p>
     *
     * @param request  请求对象
     * @param response 响应对象
     */
    @Override
    public void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 清理当前登录状态
        clearLogin(request, response);

        // redirect login page
        String loginUrl = getConfig().getLoginUrl();
        if ("".equals(loginUrl)) {
            Dict data = Dict.create().set("code", HttpStatus.HTTP_NOT_AUTHORITATIVE).set("msg", "Please login").set("data", null);
            response.getWriter().write(JSONUtil.toJsonStr(data));
        } else {
            String retUrl = GXHttpUtil.getQueryString(request, getConfig().getEncoding());
            log.debug("loginAgain redirect pageUrl.." + retUrl);
            response.sendRedirect(GXHttpUtil.encodeRetURL(loginUrl, getConfig().getParamReturnUrl(), retUrl));
        }
    }

    /**
     * SSO 退出登录
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // delete cookie
        logout(request, response, getConfig().getCache());

        // redirect logout page
        String logoutUrl = getConfig().getLogoutUrl();
        if ("".equals(logoutUrl)) {
            response.getWriter().write("sso.yml Must include: sso.config.logout.url");
        } else {
            response.sendRedirect(logoutUrl);
        }
    }
}