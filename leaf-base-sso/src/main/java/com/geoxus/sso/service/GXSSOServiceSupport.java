/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.geoxus.sso.service;

import com.geoxus.core.common.exception.GXException;
import com.geoxus.sso.cache.GXSSOCache;
import com.geoxus.sso.config.GXSSOConfig;
import com.geoxus.sso.constant.GXSSOConstant;
import com.geoxus.sso.enums.GXTokenFlag;
import com.geoxus.sso.plugins.GXSSOPlugin;
import com.geoxus.sso.security.token.GXSSOToken;
import com.geoxus.sso.security.token.GXToken;
import com.geoxus.sso.util.GXBrowserUtil;
import com.geoxus.sso.util.GXCookieHelperUtil;
import com.geoxus.sso.util.GXIpHelperUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * SSO 单点登录服务支持类
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
@Slf4j
public class GXSSOServiceSupport {
    /**
     * 获取Sso配置
     */
    public GXSSOConfig getConfig() {
        return GXSSOConfig.getInstance();
    }

    // ------------------------------- 客户端相关方法 -------------------------------

    /**
     * 获取当前请求 SSOToken
     * <p>
     * 此属性在过滤器拦截器中设置，业务系统中调用有效
     * </p>
     *
     * @param request 请求对象
     * @return SSOToken {@link GXSSOToken}
     */
    @SuppressWarnings("unchecked")
    public <T extends GXSSOToken> T attrSSOToken(HttpServletRequest request) {
        return (T) request.getAttribute(GXSSOConstant.SSO_TOKEN_ATTR);
    }

    /**
     * SSOToken 是否缓存处理逻辑
     * <p>
     * 判断 SSOToken 是否缓存 ， 如果缓存不存退出登录
     *
     * @param request 请求对象
     * @return SSOToken {@link GXSSOToken}
     */
    protected GXSSOToken cacheSSOToken(HttpServletRequest request, GXSSOCache cache) {
        // 如果缓存不存退出登录
        if (cache != null) {
            GXSSOToken cookieSSOToken = getSSOTokenFromCookie(request);
            if (cookieSSOToken == null) {
                // 未登录
                log.debug("用户未登录....");
                return null;
            }

            GXSSOToken cacheSSOToken = cache.get(cookieSSOToken.toCacheKey(), getConfig().getCacheExpires());
            if (Objects.isNull(cacheSSOToken)) {
                // 开启缓存且失效，清除 Cookie 退出 , 返回 null
                log.debug("cacheSSOToken GXSsoToken is null.");
                return null;
            } else {
                // 开启缓存，判断是否宕机：
                // 1、缓存正常，返回 tk
                // 2、缓存宕机，执行读取 Cookie 逻辑
                if (cacheSSOToken.getFlag() != GXTokenFlag.CACHE_SHUT) {
                    // 验证 cookie 与 cache 中 SSOToken 登录时间是否
                    // 不一致返回 null
                    if (cookieSSOToken.getTime() / GXSSOConstant.TOKEN_TIMESTAMP_CUT == cacheSSOToken.getTime() / GXSSOConstant.TOKEN_TIMESTAMP_CUT) {
                        return cacheSSOToken;
                    } else {
                        log.debug("Login time is not consistent or kicked out.");
                        request.setAttribute(GXSSOConstant.SSO_KICK_FLAG, GXSSOConstant.SSO_KICK_USER);
                        return null;
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
     * </p>
     *
     * @param request    请求对象
     * @param cookieName Cookie名称
     * @return GXSsoToken
     */
    protected GXSSOToken getSSOToken(HttpServletRequest request, String cookieName) {
        String accessToken = request.getHeader(getConfig().getAccessTokenName());
        if (null == accessToken || "".equals(accessToken)) {
            Cookie cookie = GXCookieHelperUtil.findCookieByName(request, cookieName);
            if (null == cookie) {
                log.debug("Unauthorized login request, ip=" + GXIpHelperUtil.getIpAddr(request));
                return null;
            }
            return GXSSOToken.parser(cookie.getValue(), false);
        }
        return GXSSOToken.parser(accessToken, true);
    }

    /**
     * <p>
     * 校验SSOToken IP 浏览器 与登录一致
     * </p>
     *
     * @param request
     * @param ssoToken 登录票据
     * @return SSOToken {@link GXSSOToken}
     */
    protected GXSSOToken checkIpBrowser(HttpServletRequest request, GXSSOToken ssoToken) {
        if (null == ssoToken) {
            return null;
        }
        // 判断请求浏览器是否合法
        if (getConfig().isCookieBrowser() && !GXBrowserUtil.isLegalUserAgent(request, ssoToken.getUserAgent())) {
            log.info("The request browser is inconsistent.");
            return null;
        }
        // 判断请求 IP 是否合法
        if (getConfig().isCookieCheckIp()) {
            String ip = GXIpHelperUtil.getIpAddr(request);
            if (ip != null && !ip.equals(ssoToken.getIp())) {
                log.info(String.format("ip inconsistent! return SSOToken null, SSOToken userIp:%s, reqIp:%s", ssoToken.getIp(), ip));
                return null;
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
    public GXSSOToken getSSOTokenFromCookie(HttpServletRequest request) {
        GXSSOToken token = attrSSOToken(request);
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
    protected Cookie generateCookie(HttpServletRequest request, GXToken token) {
        try {
            Cookie cookie = new Cookie(getConfig().getCookieName(), token.getToken());
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
            throw new GXException("Generate sso cookie exception ", e);
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
            GXSSOToken token = getSSOTokenFromCookie(request);
            if (token != null) {
                boolean rlt = cache.delete(token.toCacheKey());
                if (!rlt) {
                    cache.delete(token.toCacheKey());
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
