package com.geoxus.sso.service;

import com.geoxus.sso.security.token.GXSSOToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SSO 单点登录服务
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public interface GXSSOService {
    /**
     * <p>
     * 获取登录 SSOToken
     * </p>
     */
    GXSSOToken getSSOToken(HttpServletRequest request);

    /**
     * 踢出 指定用户 ID 的登录用户，退出当前系统。
     *
     * @param userId 用户ID
     * @return boolean
     */
    boolean kickLogin(Object userId);

    /**
     * 设置登录 Cookie
     */
    void setCookie(HttpServletRequest request, HttpServletResponse response, GXSSOToken ssoToken);

    /**
     * 清理登录状态
     */
    boolean clearLogin(HttpServletRequest request, HttpServletResponse response);

    /**
     * 退出并跳至登录页
     */
    void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException;
}