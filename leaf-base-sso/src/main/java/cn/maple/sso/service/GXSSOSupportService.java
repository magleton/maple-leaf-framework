package cn.maple.sso.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.sso.cache.GXSSOCache;
import cn.maple.sso.constant.GXSSOConstant;
import cn.maple.sso.enums.GXTokenFlag;
import cn.maple.sso.plugins.GXSSOPlugin;
import cn.maple.sso.properties.GXSSOProperties;
import cn.maple.sso.utils.GXBrowserUtil;
import cn.maple.sso.utils.GXCookieHelperUtil;
import cn.maple.sso.utils.GXIpHelperUtil;
import cn.maple.sso.utils.GXSSOHelperUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * SSO 单点登录服务支持类
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
@Slf4j
public abstract class GXSSOSupportService {
    /**
     * 获取Sso配置
     */
    public GXSSOProperties getConfig() {
        return GXSSOProperties.getInstance();
    }

    // ------------------------------- 客户端相关方法 -------------------------------

    /**
     * 获取当前请求 SSOToken
     * <p>
     * 此属性在过滤器拦截器中设置，业务系统中调用有效
     * </p>
     *
     * @param request 请求对象
     * @return Dict
     */
    public Dict attrSSOToken(HttpServletRequest request) {
        Object attribute = request.getAttribute(GXSSOConstant.SSO_TOKEN_ATTR);
        return Convert.convert(Dict.class, attribute);
    }

    /**
     * SSOToken 是否缓存处理逻辑
     * <p>
     * 判断 SSOToken 是否缓存 ， 如果缓存不存退出登录
     *
     * @param request 请求对象
     * @return Dict
     */
    protected Dict cacheSSOToken(HttpServletRequest request, GXSSOCache cache) {
        // 如果缓存不存退出登录
        if (cache != null) {
            Dict ssoToken = getSSOTokenFromCookie(request);
            if (ssoToken == null) {
                // 未登录
                log.debug("用户未登录....");
                return Dict.create();
            }

            Dict cacheSSOToken = cache.get(getConfig().getCacheExpires(), ssoToken);
            if (cacheSSOToken.isEmpty()) {
                // 开启缓存且失效，清除 Cookie 退出 , 返回 null
                log.debug("cacheSSOToken GXSsoToken is null.");
                return Dict.create();
            } else {
                // 开启缓存，判断是否宕机：
                // 1、缓存正常，返回 tk
                // 2、缓存宕机，执行读取 Cookie 逻辑
                if (!Objects.equals(cacheSSOToken.getInt("flag"), GXTokenFlag.CACHE_SHUT.value())) {
                    // 验证 cookie 与 cache 中 SSOToken 登录时间是否
                    // 不一致返回 null
                    Long cookieSSOTime = Optional.ofNullable(ssoToken.getLong("loginAt")).orElse(0L);
                    Long cacheSSOTime = Optional.ofNullable(cacheSSOToken.getLong("loginAt")).orElse(1L);
                    if (cookieSSOTime.equals(cacheSSOTime)) {
                        return cacheSSOToken;
                    } else {
                        log.debug("Login time is not consistent or kicked out.");
                        request.setAttribute(GXSSOConstant.SSO_KICK_FLAG, GXSSOConstant.SSO_KICK_USER);
                        return Dict.create();
                    }
                }
            }
        }

        // GXSsoCache 为 null 执行以下逻辑
        return getSSOToken(request, getConfig().getCookieName());
    }

    /**
     * <p>
     * 获取当前请求 SSOToken
     * 1、 先从header中获取
     * 2、 再从cookie中获取
     * </p>
     *
     * @param request    请求对象
     * @param cookieName Cookie名称
     * @return GXSsoToken
     */
    protected Dict getSSOToken(HttpServletRequest request, String cookieName) {
        String token = request.getHeader(getConfig().getTokenName());
        if (null == token || "".equals(token)) {
            Cookie cookie = GXCookieHelperUtil.findCookieByName(request, cookieName);
            if (null == cookie) {
                log.debug("Unauthorized login request, ip=" + GXIpHelperUtil.getIpAddr(request));
                return Dict.create();
            }
            return GXSSOHelperUtil.parser(cookie.getValue(), false);
        }
        return GXSSOHelperUtil.parser(token, true);
    }

    /**
     * <p>
     * 校验SSOToken IP 浏览器 与登录一致
     * </p>
     *
     * @param request  请求对象
     * @param ssoToken 登录票据
     * @return Dict
     */
    protected Dict checkIpBrowser(HttpServletRequest request, Dict ssoToken) {
        if (null == ssoToken) {
            return Dict.create();
        }
        // 判断请求浏览器是否合法
        if (getConfig().isCookieBrowser() && !GXBrowserUtil.isLegalUserAgent(request, ssoToken.getStr("userAgent"))) {
            log.debug("The request browser is inconsistent.");
            return Dict.create();
        }
        // 判断请求 IP 是否合法
        if (getConfig().isCookieCheckIp()) {
            String ip = GXIpHelperUtil.getIpAddr(request);
            if (ip != null && !ip.equals(ssoToken.getStr("ip"))) {
                log.debug(String.format("ip inconsistent! return SSOToken null, SSOToken userIp:%s, reqIp:%s", ssoToken.getStr("ip"), ip));
                return Dict.create();
            }
        }
        return ssoToken;
    }

    /**
     * cookie 中获取 SSOToken, 该方法未验证 IP 等其他信息。
     * <p>
     * 1、自动设置
     * 2、拦截器 request 中获取
     * 3、解密 Cookie 获取
     * </p>
     *
     * @param request HTTP 请求
     * @return GXSsoToken
     */
    public Dict getSSOTokenFromCookie(HttpServletRequest request) {
        Dict token = attrSSOToken(request);
        if (token == null) {
            token = getSSOToken(request, getConfig().getCookieName());
        }
        return token;
    }

    // ------------------------------- 登录相关方法 -------------------------------

    /**
     * 根据SSOToken生成登录信息Cookie
     *
     * @param request 请求参数
     * @param token   SSO 登录信息票据
     * @return Cookie 登录信息Cookie {@link Cookie}
     */
    protected Cookie generateCookie(HttpServletRequest request, Dict token) {
        try {
            Cookie cookie = new Cookie(getConfig().getCookieName(), token.getStr("token"));
            cookie.setPath(getConfig().getCookiePath());
            cookie.setSecure(getConfig().isCookieSecure());
            // domain 提示
            // 有些浏览器 localhost 无法设置 cookie
            String domain = getConfig().getCookieDomain();
            if (null != domain) {
                cookie.setDomain(domain);
                if ("".equals(domain) || domain.contains("localhost")) {
                    log.warn("if you can't login, please enter normal domain. instead:" + domain);
                }
            }

            // 设置Cookie超时时间
            int maxAge = getConfig().getCookieMaxAge();
            Integer attrMaxAge = (Integer) request.getAttribute(GXSSOConstant.SSO_COOKIE_MAX_AGE);
            if (attrMaxAge != null) {
                maxAge = attrMaxAge;
            }
            if (maxAge >= 0) {
                cookie.setMaxAge(maxAge);
            }
            return cookie;
        } catch (Exception e) {
            throw new GXBusinessException("Generate sso cookie exception ", e);
        }
    }

    /**
     * <p>
     * 退出当前登录状态
     * </p>
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return boolean true 成功, false 失败
     */
    protected boolean logout(HttpServletRequest request, HttpServletResponse response, GXSSOCache cache) {
        // SSOToken 如果开启了缓存，删除缓存记录
        if (cache != null && !GXSSOConstant.SSO_KICK_USER.equals(request.getAttribute(GXSSOConstant.SSO_KICK_FLAG))) {
            Dict token = getSSOTokenFromCookie(request);
            if (token != null) {
                boolean rlt = cache.delete(token);
                if (!rlt) {
                    cache.delete(token);
                }
            }
        }

        // 执行插件逻辑
        List<GXSSOPlugin> pluginList = getConfig().getPluginList();
        if (pluginList != null) {
            for (GXSSOPlugin plugin : pluginList) {
                boolean logout = plugin.logout(request, response);
                if (!logout) {
                    plugin.logout(request, response);
                }
            }
        }

        // 删除登录 Cookie
        return GXCookieHelperUtil.clearCookieByName(request, response, getConfig().getCookieName(), getConfig().getCookieDomain(), getConfig().getCookiePath());
    }
}
