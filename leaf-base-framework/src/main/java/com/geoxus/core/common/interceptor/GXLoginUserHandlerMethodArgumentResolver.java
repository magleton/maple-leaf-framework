package com.geoxus.core.common.interceptor;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXLoginUserAnnotation;
import com.geoxus.core.common.constant.GXTokenConstants;
import com.geoxus.core.common.entity.GXUUserEntity;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.service.GXUUserService;
import com.geoxus.core.common.util.GXSpringContextUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

/**
 * 有@GXLoginUserAnnotation注解的方法参数，注入当前登录用户
 */
@Component
public class GXLoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().getSuperclass().isAssignableFrom(GXUUserEntity.class)
                && parameter.hasParameterAnnotation(GXLoginUserAnnotation.class);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        // 获取用户ID
        Object object = request.getAttribute(GXTokenConstants.USER_ID, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            final String header = request.getHeader(GXTokenConstants.USER_TOKEN);
            if (null == header) {
                return null;
            }
            final Dict tokenData = GXTokenManager.decodeUserToken(header);
            object = tokenData.getObj(GXTokenConstants.USER_ID);
            if (null == object) {
                return null;
            }
        }
        // 获取用户信息
        return Objects.requireNonNull(GXSpringContextUtils.getBean(GXUUserService.class)).getById(Convert.toLong(object));
    }
}
