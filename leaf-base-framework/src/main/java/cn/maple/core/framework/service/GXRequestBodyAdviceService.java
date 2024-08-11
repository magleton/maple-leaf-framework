package cn.maple.core.framework.service;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.TypeUtil;
import cn.maple.core.framework.dto.protocol.req.GXBaseReqProtocol;
import cn.maple.core.framework.util.GXCommonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

public interface GXRequestBodyAdviceService {
    /**
     * 进行参数验证之前对数据进行修复的方法名字
     */
    String BEFORE_REPAIR_METHOD = "beforeRepair";

    /**
     * 进行参数验证
     */
    String VERIFY_METHOD = "verify";

    /**
     * 进行参数验证之后对数据进行修复的方法名字
     */
    String AFTER_REPAIR_METHOD = "afterRepair";

    /**
     * Invoked first to determine if this interceptor applies.
     *
     * @param methodParameter the method parameter
     * @param targetType      the target type, not necessarily the same as the method
     *                        parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType   the selected converter type
     * @return whether this interceptor should be invoked or not
     */
    default boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ClassUtil.isAssignable(GXBaseReqProtocol.class, TypeUtil.getClass(targetType));
    }

    /**
     * Invoked third (and last) after the request body is converted to an Object.
     *
     * @param body          set to the converter Object before the first advice is called
     * @param inputMessage  the request
     * @param parameter     the target method parameter
     * @param targetType    the target type, not necessarily the same as the method
     *                      parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the converter used to deserialize the body
     * @return the same body or a new instance
     */
    default Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 对请求数据进行验证之前的修复处理
        GXCommonUtils.reflectCallObjectMethod(body, BEFORE_REPAIR_METHOD);
        // 调用目标bean对象的验证方法对数据进行验证(自定义验证)
        GXCommonUtils.reflectCallObjectMethod(body, VERIFY_METHOD);
        // 调用目标bean对象的修复方法对数据进行最后的修复
        GXCommonUtils.reflectCallObjectMethod(body, AFTER_REPAIR_METHOD);
        return body;
    }

    /**
     * Invoked second before the request body is read and converted.
     *
     * @param inputMessage  the request
     * @param parameter     the target method parameter
     * @param targetType    the target type, not necessarily the same as the method
     *                      parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the converter used to deserialize the body
     * @return the input request or a new instance (never {@code null})
     */
    default HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    /**
     * Invoked second (and last) if the body is empty.
     *
     * @param body          usually set to {@code null} before the first advice is called
     * @param inputMessage  the request
     * @param parameter     the method parameter
     * @param targetType    the target type, not necessarily the same as the method
     *                      parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the selected converter type
     * @return the value to use, or {@code null} which may then raise an
     * {@code HttpMessageNotReadableException} if the argument is required
     */
    default Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
