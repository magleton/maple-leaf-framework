package cn.maple.sso.web.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.core.framework.web.interceptor.GXBaseSSOPermissionInterceptor;
import cn.maple.sso.annotation.GXPermissionAnnotation;
import cn.maple.sso.enums.GXAction;
import cn.maple.sso.oauth.GXSSOAuthorization;
import cn.maple.sso.properties.GXSSOProperties;
import cn.maple.sso.utils.GXHttpUtil;
import cn.maple.sso.utils.GXSSOHelperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 权限拦截器（必须在 sso 拦截器之后执行）
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
@Component
@Slf4j
public class GXSSOPermissionInterceptor extends GXBaseSSOPermissionInterceptor {
    /*
     * 非法请求提示 URL
     */
    private String illegalUrl;

    /**
     * 无注解情况下，设置为true，不进行权限验证
     */
    private boolean nothingAnnotationPass = true;

    /**
     * 用户权限验证
     * 方法拦截 Controller 处理之前进行调用。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            Dict tokenDict = GXSSOHelperUtil.attrToken(request);
            if (CollUtil.isEmpty(tokenDict)) {
                return true;
            }

            // 权限验证合法
            if (isVerification(request, handler, tokenDict)) {
                return true;
            }

            //  无权限访问
            return unauthorizedAccess(request, response);
        }

        return true;
    }

    /**
     * <p>
     * 判断权限是否合法，支持 1、请求地址 2、注解编码
     * </p>
     *
     * @param request 请求对象
     * @param handler 处理的Handler
     * @param token   token
     * @return boolean
     */
    protected boolean isVerification(HttpServletRequest request, Object handler, Dict token) {
        // URL 权限认证
        if (GXSSOProperties.getInstance().isPermissionUri()) {
            String uri = request.getRequestURI();
            if (uri == null || this.getAuthorization().isPermitted(token, uri)) {
                return true;
            }
        }
        // 注解权限认证
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        GXPermissionAnnotation pm = method.getAnnotation(GXPermissionAnnotation.class);
        // 无注解情况下，设置为true，不进行期限验证
        if (pm != null) {
            // 权限合法
            if (pm.action() == GXAction.Skip) {
                // 忽略拦截
                return true;
            } else {
                return !"".equals(pm.value()) && this.getAuthorization().isPermitted(token, pm.value());
            }
        } else {
            return this.isNothingAnnotationPass();
        }
        // 非法访问
        // todo
    }

    /**
     * <p>
     * 无权限访问处理，默认返回 403  ，illegalUrl 非空重定向至该地址
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return boolean
     */
    protected boolean unauthorizedAccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug(" request 403 url: " + request.getRequestURI());
        if (GXHttpUtil.isAjax(request)) {
            // AJAX 请求 403 未授权访问提示
            GXHttpUtil.ajaxStatus(response, 403, "ajax Unauthorized access.");
        } else {
            // 正常 HTTP 请求
            if (this.getIllegalUrl() == null || "".equals(this.getIllegalUrl())) {
                response.sendError(403, "Forbidden");
            } else {
                response.sendRedirect(this.getIllegalUrl());
            }
        }
        return false;
    }

    public GXSSOAuthorization getAuthorization() {
        GXSSOAuthorization authorization = GXSpringContextUtils.getBean(GXSSOAuthorization.class);
        if (Objects.isNull(authorization)) {
            throw new GXBusinessException("请实现GXSSOAuthorization接口,并将其放入Soring容器中");
        }
        return authorization;
    }

    public String getIllegalUrl() {
        return illegalUrl;
    }

    public void setIllegalUrl(String illegalUrl) {
        this.illegalUrl = illegalUrl;
    }

    public boolean isNothingAnnotationPass() {
        return nothingAnnotationPass;
    }

    public void setNothingAnnotationPass(boolean nothingAnnotationPass) {
        this.nothingAnnotationPass = nothingAnnotationPass;
    }
}
