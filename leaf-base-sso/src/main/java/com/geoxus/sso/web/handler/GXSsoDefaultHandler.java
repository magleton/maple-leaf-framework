package com.geoxus.sso.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * SSO 默认拦截处理器，自定义 Handler 可继承该类。
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-15
 */
public class GXSsoDefaultHandler implements GXSsoHandler {
    private static GXSsoDefaultHandler handler;

    /**
     * new 当前对象
     */
    public static GXSsoDefaultHandler getInstance() {
        if (handler == null) {
            handler = new GXSsoDefaultHandler();
        }
        return handler;
    }

    /**
     * 未登录时，处理 AJAX 请求。
     * <p>
     * 返回 HTTP 状态码 401（未授权） 请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。
     * </p>
     */
    public boolean preTokenIsNullAjax(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().write("{code:\"logout\", msg:\"Have logout\"}");
        } catch (IOException e) {
            // to do nothing
        }
        return false;
    }

    public boolean preTokenIsNull(HttpServletRequest request, HttpServletResponse response) {
        /* 预留子类处理 */
        return true;
    }
}
