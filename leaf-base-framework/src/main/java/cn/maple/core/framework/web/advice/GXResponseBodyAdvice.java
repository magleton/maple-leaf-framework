package cn.maple.core.framework.web.advice;

import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.framework.service.GXResponseBodyAdviceService;
import cn.maple.core.framework.util.GXResultUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

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
        response.getHeaders().add("AUTHOR", "magleton");
        GXResponseBodyAdviceService responseBodyAdviceService = GXSpringContextUtils.getBean(GXResponseBodyAdviceService.class);
        if (ObjectUtil.isNotNull(responseBodyAdviceService)) {
            return responseBodyAdviceService.beforeBodyWrite(body, returnType, selectedContentType, selectedConverterType, request, response);
        }
        return body;
    }
}