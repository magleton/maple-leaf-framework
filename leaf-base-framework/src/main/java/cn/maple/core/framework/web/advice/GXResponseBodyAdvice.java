package cn.maple.core.framework.web.advice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.framework.service.GXResponseBodyAdviceService;
import cn.maple.core.framework.util.GXAuthCodeUtils;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXResultUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.server.Cookie;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@Log4j2
@RestControllerAdvice
public class GXResponseBodyAdvice implements ResponseBodyAdvice<Object> /* implements ResponseBodyAdvice<GXResultUtils<?>>*/ {
    /**
     * Whether this component supports the given controller method return type
     * and the selected {@code HttpMessageConverter} type.
     *
     * @param returnType    the return type
     * @param converterType the selected converter type
     * @return {@code true} if {@link #beforeBodyWrite} should be invoked;
     * {@code false} otherwise
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        GXResponseBodyAdviceService responseBodyAdviceService = GXSpringContextUtils.getBean(GXResponseBodyAdviceService.class);
        if (ObjectUtil.isNotNull(responseBodyAdviceService)) {
            return responseBodyAdviceService.supports(returnType, converterType);
        }
        return returnType.getParameterType().isAssignableFrom(GXResultUtils.class);
    }

    /**
     * Invoked after an {@code HttpMessageConverter} is selected and just before
     * its write method is invoked.
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     * @return the body that was passed in or a modified (possibly new) instance
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.debug("响应拦截成功!");
        boolean allowCredentials = GXCommonUtils.getEnvironmentValue("cors.allow.credentials", boolean.class, false);
        if (allowCredentials) {
            List<String> cookies = buildCookies();
            cookies.forEach(cookie -> response.getHeaders().add(HttpHeaders.SET_COOKIE, cookie));
        }
        GXResponseBodyAdviceService responseBodyAdviceService = GXSpringContextUtils.getBean(GXResponseBodyAdviceService.class);
        if (ObjectUtil.isNotNull(responseBodyAdviceService)) {
            return responseBodyAdviceService.beforeBodyWrite(body, returnType, selectedContentType, selectedConverterType, request, response);
        }
        return body;
    }

    /**
     * 构建响应Cookie
     *
     * @return Cookie对象
     */
    private List<String> buildCookies() {
        GXResponseBodyAdviceService responseBodyAdviceService = GXSpringContextUtils.getBean(GXResponseBodyAdviceService.class);
        if (ObjectUtil.isNotNull(responseBodyAdviceService)) {
            return responseBodyAdviceService.buildCookies();
        }
        return defaultBuildCookies();
    }

    /**
     * 默认的Cookie构建方法
     *
     * @return Cookies
     */
    private List<String> defaultBuildCookies() {
        boolean isSecure = GXCommonUtils.getEnvironmentValue("cors.cookie.secure", boolean.class, false);
        String cookieSecret = GXCommonUtils.getEnvironmentValue("cors.cookie.secret", String.class, "CF3417E3CF6B0F28");
        Integer effectiveDuration = GXCommonUtils.getEnvironmentValue("cors.cookie.duration", Integer.class, 300);
        String cookieVirusID = GXCommonUtils.getEnvironmentValue("cors.cookie.virusId", String.class);
        if (CharSequenceUtil.isNotBlank(cookieVirusID)) {
            String authCode = GXAuthCodeUtils.authCodeEncode(cookieVirusID, cookieSecret, effectiveDuration);
            ResponseCookie cookie = ResponseCookie.from("UserData", authCode)
                    .maxAge(-1)// 浏览器关闭，则删除 Cookie
                    .secure(isSecure)       // 可以在HTTP或者HTTPS协议中传输
                    .httpOnly(true)         // javascript不能读写
                    //.domain(null)		    // 提交cookie的域
                    //.path(null)		    // 提交cookie的path
                    .sameSite(Cookie.SameSite.LAX.attributeValue())// 设置 SameSite 为 LAX
                    .build();
            return CollUtil.newArrayList(cookie.toString());
        }
        return CollUtil.newArrayList();
    }
}