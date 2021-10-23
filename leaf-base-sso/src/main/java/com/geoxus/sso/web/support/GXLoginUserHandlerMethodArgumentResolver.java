package com.geoxus.sso.web.support;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.framework.constant.GXTokenConstant;
import com.geoxus.core.framework.util.GXAuthCodeUtil;
import com.geoxus.core.framework.util.GXSpringContextUtil;
import com.geoxus.core.framework.web.support.GXCustomerHandlerMethodArgumentResolver;
import com.geoxus.sso.annotation.GXLoginUserAnnotation;
import com.geoxus.sso.dto.GXUserInfoDto;
import com.geoxus.sso.service.GXUUserService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

/**
 * 有@GXLoginUserAnnotation注解的方法参数，注入当前登录用户
 */
@Component
public class GXLoginUserHandlerMethodArgumentResolver implements GXCustomerHandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        /*return parameter.getParameterType().getSuperclass().isAssignableFrom(GXUUserEntity.class)
                && parameter.hasParameterAnnotation(GXLoginUserAnnotation.class);*/
        return parameter.getParameterType().getSuperclass().isAssignableFrom(GXUserInfoDto.class)
                && parameter.hasParameterAnnotation(GXLoginUserAnnotation.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        // 获取用户ID
        Object object = request.getAttribute(GXTokenConstant.USER_ID, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            final String header = request.getHeader(GXTokenConstant.USER_TOKEN);
            if (null == header) {
                return null;
            }
            final Dict tokenData = JSONUtil.toBean(GXAuthCodeUtil.authCodeDecode(header, GXTokenConstant.KEY), Dict.class);
            object = tokenData.getObj(GXTokenConstant.USER_ID);
            if (null == object) {
                return null;
            }
        }
        // 获取用户信息
        Long userId = Convert.toLong(object);
        return Objects.requireNonNull(GXSpringContextUtil.getBean(GXUUserService.class)).getUserByUserId(userId);
    }
}
