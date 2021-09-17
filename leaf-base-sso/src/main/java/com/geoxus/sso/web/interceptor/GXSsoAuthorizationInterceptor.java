package com.geoxus.sso.web.interceptor;

import com.geoxus.core.common.interceptor.GXAuthorizationInterceptor;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.sso.annotation.GXLoginAnnotation;
import com.geoxus.sso.cache.GXSsoCache;
import com.geoxus.sso.constant.GXSsoConstant;
import com.geoxus.sso.plugins.GXSsoPlugin;
import com.geoxus.sso.properties.GXSsoConfigProperties;
import com.geoxus.sso.security.token.GXSsoToken;
import com.geoxus.sso.util.GXHttpUtil;
import com.geoxus.sso.util.GXSsoHelperUtil;
import com.geoxus.sso.web.handler.GXSsoDefaultHandler;
import com.geoxus.sso.web.handler.GXSsoHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * 前端用户登陆验证
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class GXSsoAuthorizationInterceptor extends GXAuthorizationInterceptor {
    /**
     * SSO 处理器
     */
    private GXSsoHandler handler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        GXLoginAnnotation annotation;
        if (request.getMethod().equalsIgnoreCase("OPTIONS") || !(handler instanceof HandlerMethod)) {
            return true;
        }

        annotation = ((HandlerMethod) handler).getMethodAnnotation(GXLoginAnnotation.class);
        if (Objects.isNull(annotation)) {
            // 没有标注需要登陆的接口直接放行通过
            return true;
        }

        // 为每个应用配置属于自己的SsoConfig对象
        GXSsoConfigProperties ssoConfigProperties = GXSpringContextUtils.getBean(GXSsoConfigProperties.class);
        if (Objects.nonNull(ssoConfigProperties.getConfig())) {
            GXSsoHelperUtil.setSsoConfig(ssoConfigProperties.getConfig());
        }
        // 为每个应用配置自己的插件
        Map<String, GXSsoPlugin> ssoPluginMap = GXSpringContextUtils.getBeans(GXSsoPlugin.class);
        if (!ssoPluginMap.isEmpty()) {
            ArrayList<GXSsoPlugin> plugins = new ArrayList<>();
            ssoPluginMap.forEach((key, val) -> {
                plugins.add(val);
            });
            GXSsoHelperUtil.getSsoConfig().setPluginList(plugins);
        }
        // 为每个应用配置自己的SsoCache实例
        if (Objects.nonNull(GXSpringContextUtils.getBean(GXSsoCache.class))) {
            GXSsoHelperUtil.getSsoConfig().setCache(GXSpringContextUtils.getBean(GXSsoCache.class));
        }
        // 获取SsoToken对象
        GXSsoToken ssoToken = GXSsoHelperUtil.getSsoToken(request);

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
                    GXSsoHelperUtil.clearRedirectLogin(request, response);
                }
                return false;
            }
        } else {
            // 正常请求，request 设置 token 减少二次解密
            request.setAttribute(GXSsoConstant.SSO_TOKEN_ATTR, ssoToken);
        }
        return true;
    }

    public GXSsoHandler getHandler() {
        if (handler == null) {
            return GXSsoDefaultHandler.getInstance();
        }
        return handler;
    }

    public void setHandler(GXSsoHandler handler) {
        this.handler = handler;
    }
}
