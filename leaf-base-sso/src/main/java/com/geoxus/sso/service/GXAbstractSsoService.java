package com.geoxus.sso.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.geoxus.sso.cache.GXSsoCache;
import com.geoxus.sso.config.GXSsoConfig;
import com.geoxus.sso.enums.GXTokenFlag;
import com.geoxus.sso.plugins.GXSsoPlugin;
import com.geoxus.sso.security.token.GXSsoToken;
import com.geoxus.sso.util.GXCookieHelperUtil;
import com.geoxus.sso.util.GXHttpUtil;
import com.geoxus.sso.util.GXRandomUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * SSO 单点登录服务抽象实现类
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
@Slf4j
public abstract class GXAbstractSsoService extends GXSsoServiceSupport implements GXSsoService {
    /**
     * 获取当前请求 GXSsoToken
     * 从 Cookie 解密 GXSsoToken 使用场景, 拦截器
     * 非拦截器建议使用 attrSSOToken 减少二次解密
     *
     * @param request 请求对象
     * @return GXSsoToken {@link GXSsoToken}
     */
    @Override
    public GXSsoToken getSsoToken(HttpServletRequest request) {
        GXSsoToken token = checkIpBrowser(request, cacheSSOToken(request, config.getCache()));
        // 执行插件逻辑
        List<GXSsoPlugin> pluginList = config.getPluginList();
        if (pluginList != null) {
            for (GXSsoPlugin plugin : pluginList) {
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
        GXSsoCache cache = config.getCache();
        if (cache != null) {
            return cache.delete(GXSsoConfig.toCacheKey(userId));
        } else {
            log.info(" kickLogin! please implements GXSsoCache class.");
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
    public void setCookie(HttpServletRequest request, HttpServletResponse response, GXSsoToken ssoToken) {
        // 设置加密 Cookie
        Cookie ck = this.generateCookie(request, ssoToken);

        // 判断 GXSsoCache 是否缓存处理失效
        // cache 缓存宕机，flag 设置为失效
        GXSsoCache cache = config.getCache();
        if (cache != null) {
            boolean rlt = cache.set(ssoToken.toCacheKey(), ssoToken, config.getCacheExpires());
            if (!rlt) {
                ssoToken.setFlag(GXTokenFlag.CACHE_SHUT);
            }
        }

        //执行插件逻辑
        List<GXSsoPlugin> pluginList = config.getPluginList();
        if (pluginList != null) {
            for (GXSsoPlugin plugin : pluginList) {
                boolean login = plugin.login(request, response);
                if (!login) {
                    plugin.login(request, response);
                }
            }
        }

        // Cookie设置HttpOnly
        if (config.isCookieHttpOnly()) {
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
    public void authCookie(HttpServletRequest request, HttpServletResponse response, GXSsoToken ssoToken) {
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
        return logout(request, response, config.getCache());
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
        String loginUrl = config.getLoginUrl();
        if ("".equals(loginUrl)) {
            Dict data = Dict.create().set("code", HttpStatus.HTTP_NOT_AUTHORITATIVE).set("msg", "Please login").set("data", null);
            response.getWriter().write(JSONUtil.toJsonStr(data));
        } else {
            String retUrl = GXHttpUtil.getQueryString(request, config.getEncoding());
            log.debug("loginAgain redirect pageUrl.." + retUrl);
            response.sendRedirect(GXHttpUtil.encodeRetURL(loginUrl, config.getParamReturnUrl(), retUrl));
        }
    }

    /**
     * SSO 退出登录
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // delete cookie
        logout(request, response, config.getCache());

        // redirect logout page
        String logoutUrl = config.getLogoutUrl();
        if ("".equals(logoutUrl)) {
            response.getWriter().write("sso.properties Must include: sso.logout.url");
        } else {
            response.sendRedirect(logoutUrl);
        }
    }
}