package cn.maple.sso.web.support;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXTokenConstant;
import cn.maple.core.framework.util.GXAuthCodeUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.core.framework.web.support.GXCustomerHandlerMethodArgumentResolver;
import cn.maple.sso.annotation.GXLoginUserAnnotation;
import cn.maple.sso.dto.GXUserInfoDto;
import cn.maple.sso.service.GXUUserService;
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
        Object object = request.getAttribute(GXTokenConstant.TOKEN_USER_ID_FIELD_NAME, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            final String header = request.getHeader(GXTokenConstant.USER_TOKEN_NAME);
            if (null == header) {
                return null;
            }
            final Dict tokenData = JSONUtil.toBean(GXAuthCodeUtils.authCodeDecode(header, GXTokenConstant.USER_TOKEN_SECRET_KEY), Dict.class);
            object = tokenData.getObj(GXTokenConstant.TOKEN_USER_ID_FIELD_NAME);
            if (null == object) {
                return null;
            }
        }
        // 获取用户信息
        Long userId = Convert.toLong(object);
        return Objects.requireNonNull(GXSpringContextUtils.getBean(GXUUserService.class)).getUserByUserId(userId);
    }
}
