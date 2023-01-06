package cn.maple.sso.web.interceptor;

import cn.maple.core.framework.web.interceptor.GXAuthorizationInterceptor;
import cn.maple.sso.annotation.GXLoginAnnotation;
import cn.maple.sso.constant.GXSSOConstant;
import cn.maple.sso.security.token.GXSSOToken;
import cn.maple.sso.utils.GXHttpUtil;
import cn.maple.sso.utils.GXSSOHelperUtil;
import cn.maple.sso.web.handler.GXSSODefaultHandler;
import cn.maple.sso.web.handler.GXSSOHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 前端用户登陆验证
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class GXSSOAuthorizationInterceptor extends GXAuthorizationInterceptor {
    /**
     * SSO 处理器
     */
    private GXSSOHandler handler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        GXLoginAnnotation annotation;
        if (request.getMethod().equalsIgnoreCase("OPTIONS") || !(handler instanceof HandlerMethod)) {
            return true;
        }

        annotation = ((HandlerMethod) handler).getMethodAnnotation(GXLoginAnnotation.class);
        if (Objects.nonNull(annotation)) {
            // 没有标注需要登陆的接口直接放行通过
            return true;
        }

        // 获取SsoToken对象
        GXSSOToken ssoToken = GXSSOHelperUtil.getSSOToken(request);

        // 判断Token
        if (Objects.isNull(ssoToken)) {
            if (GXHttpUtil.isAjax(request)) {
                // Handler 处理 AJAX 请求
                getHandler().preTokenIsNullAjax(request, response);
                return false;
            } else {
                // token 为空, 调用 Handler 处理
                // 返回 true 继续执行, 清理登录状态并重定向至登录界面
                if (getHandler().preTokenIsNull(request, response)) {
                    log.debug("logout. request url:" + request.getRequestURL());
                    GXSSOHelperUtil.clearRedirectLogin(request, response);
                }
                return false;
            }
        } else {
            // 正常请求，request 设置 token 减少二次解密
            request.setAttribute(GXSSOConstant.SSO_TOKEN_ATTR, ssoToken);
        }
        return true;
    }

    public GXSSOHandler getHandler() {
        if (handler == null) {
            return GXSSODefaultHandler.getInstance();
        }
        return handler;
    }

    public void setHandler(GXSSOHandler handler) {
        this.handler = handler;
    }
}
