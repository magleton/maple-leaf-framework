package com.geoxus.sso.web.handler;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SSO 默认拦截处理器，自定义 Handler 可继承该类。
 *
 * @author britton britton@126.com
 * @since 2021-09-15
 */
public class GXSsoDefaultHandler implements GXSsoHandler {
    /**
     * 默认处理器
     */
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
     */
    public boolean preTokenIsNullAjax(HttpServletRequest request, HttpServletResponse response) {
        try {
            Dict data = Dict.create()
                    .set("code", HttpStatus.HTTP_NOT_AUTHORITATIVE)
                    .set("msg", "Have logout")
                    .set("data", null);
            response.getWriter().write(JSONUtil.toJsonStr(data));
            // response.getWriter().write("{code:\"logout\", msg:\"Have logout\"}");
        } catch (IOException e) {
            // to do nothing
        }
        return false;
    }

    /**
     * 处理token为NULL的情况
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return boolean
     */
    public boolean preTokenIsNull(HttpServletRequest request, HttpServletResponse response) {
        // 预留子类处理
        return true;
    }
}
