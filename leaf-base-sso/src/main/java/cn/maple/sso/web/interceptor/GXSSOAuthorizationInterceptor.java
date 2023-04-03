package cn.maple.sso.web.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.core.framework.web.interceptor.GXAuthorizationInterceptor;
import cn.maple.sso.annotation.GXLoginAnnotation;
import cn.maple.sso.cache.GXSSOCache;
import cn.maple.sso.constant.GXSSOConstant;
import cn.maple.sso.properties.GXUrlWhiteListsConfigProperties;
import cn.maple.sso.service.GXAuthorizationInterceptorService;
import cn.maple.sso.utils.GXHttpUtil;
import cn.maple.sso.utils.GXSSOHelperUtil;
import cn.maple.sso.web.handler.GXSSODefaultHandler;
import cn.maple.sso.web.handler.GXSSOHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.Resource;
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

    @Resource
    private GXUrlWhiteListsConfigProperties urlWhiteListsConfigProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        GXLoginAnnotation annotation;
        if (request.getMethod().equalsIgnoreCase("OPTIONS") || !(handler instanceof HandlerMethod)) {
            return true;
        }

        String requestURI = request.getRequestURI();
        if (CollUtil.contains(urlWhiteListsConfigProperties.getWhiteLists(), requestURI)) {
            // 白名单直接放行
            return true;
        }

        annotation = ((HandlerMethod) handler).getMethodAnnotation(GXLoginAnnotation.class);
        if (Objects.nonNull(annotation)) {
            // 没有标注需要登陆的接口直接放行通过
            return true;
        }

        // 调用业务端自定义的拦截规则
        GXAuthorizationInterceptorService authorizationInterceptorService = GXSpringContextUtils.getBean(GXAuthorizationInterceptorService.class);
        if (!Objects.isNull(authorizationInterceptorService)) {
            boolean interceptor = authorizationInterceptorService.interceptor(request, response, annotation);
            if (interceptor) {
                return interceptor;
            }
        }

        // 获取SsoToken对象
        Dict ssoToken = GXSSOHelperUtil.getSSOToken(request);

        // 判断Token
        if (CollUtil.isEmpty(ssoToken)) {
            if (GXHttpUtil.isAjax(request)) {
                // Handler 处理 AJAX 请求
                getHandler().preTokenIsNullAjax(request, response);
                // 如果启用了GXSSOCache 则调用Cache的删除方法 删除缓存中的数据
                GXSSOCache ssoCache = GXSpringContextUtils.getBean(GXSSOCache.class);
                if (Objects.nonNull(ssoCache)) {
                    ssoCache.delete(Dict.create());
                }
                return false;
            } else {
                // token 为空, 调用 Handler 处理
                // 返回 true 继续执行, 清理登录状态并重定向至登录界面
                if (getHandler().preTokenIsNull(request, response)) {
                    log.debug("logout. request url:" + request.getRequestURL());
                    GXSSOCache ssoCache = GXSpringContextUtils.getBean(GXSSOCache.class);
                    if (Objects.nonNull(ssoCache)) {
                        ssoCache.delete(Dict.create());
                    }
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
