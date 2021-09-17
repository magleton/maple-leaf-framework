package com.geoxus.sso.plugins;

import com.geoxus.sso.security.token.GXSSOToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * SSO 插件接口
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public interface GXSSOPlugin {
    /**
     * 登录时调用该方法
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return boolean
     */
    boolean login(HttpServletRequest request, HttpServletResponse response);

    /**
     * 登录后获取 Token 时调用该方法
     * 用来验证 Token 合法性（例如 time 超时验证）
     *
     * @param ssoToken 登录票据
     * @return boolean
     */
    default boolean validateToken(GXSSOToken ssoToken) {
        return true;
    }

    /**
     * <p>
     * 退出时调用该方法
     * </p>
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return boolean
     */
    boolean logout(HttpServletRequest request, HttpServletResponse response);
}