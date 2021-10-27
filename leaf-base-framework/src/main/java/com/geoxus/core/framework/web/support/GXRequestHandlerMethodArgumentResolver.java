package com.geoxus.core.framework.web.support;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.framework.annotation.GXFieldComment;
import com.geoxus.core.framework.annotation.GXMergeSingleField;
import com.geoxus.core.framework.annotation.GXRequestBody;
import com.geoxus.core.framework.exception.GXBusinessException;
import com.geoxus.core.framework.pojo.GXResultCode;
import com.geoxus.core.framework.util.GXSpringContextUtil;
import com.geoxus.core.framework.util.GXValidatorUtil;
import com.geoxus.core.framework.validator.GXValidateJSONFieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class GXRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @GXFieldComment(zhDesc = "请求中的参数名字")
    public static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    /**
     * 进行参数验证之前对数据进行修复的方法名字
     */
    private static final String BEFORE_REPAIR_METHOD = "beforeRepair";

    /**
     * 进行参数验证
     */
    private static final String VERIFY_METHOD = "verify";

    /**
     * 进行参数验证之后对数据进行修复的方法名字
     */
    private static final String AFTER_REPAIR_METHOD = "afterRepair";

    @GXFieldComment(zhDesc = "日志对象")
    private static final Logger LOGGER = LoggerFactory.getLogger(GXRequestHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(GXRequestBody.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String body = getRequestBody(webRequest);
        final Class<?> parameterType = parameter.getParameterType();
        final Object bean = Convert.convert(parameterType, JSONUtil.toBean(body, parameterType));
        final GXRequestBody requestBody = parameter.getParameterAnnotation(GXRequestBody.class);
        final String value = Objects.requireNonNull(requestBody).value();
        Set<String> jsonFields = new HashSet<>(16);
        boolean validateTarget = requestBody.validateTarget();

        // 对请求数据进行验证之前的修复处理
        callUserDefinedMethod(parameterType, bean, BEFORE_REPAIR_METHOD);

        // 填充目标bean的扩展字段属性
        dealDeclaredJsonFields(bean, parameterType, jsonFields);

        // 调用目标bean对象的验证方法对数据进行验证(自定义验证)
        callUserDefinedMethod(parameterType, bean, VERIFY_METHOD);

        // 数据验证
        Class<?>[] groups = requestBody.groups();
        if (validateTarget) {
            if (parameter.hasParameterAnnotation(Valid.class)) {
                GXValidatorUtil.validateEntity(bean, value, groups);
            } else if (parameter.hasParameterAnnotation(Validated.class)) {
                groups = Objects.requireNonNull(parameter.getParameterAnnotation(Validated.class)).value();
                GXValidatorUtil.validateEntity(bean, value, groups);
            }
        }

        // 调用目标bean对象的修复方法对数据进行最后的修复
        callUserDefinedMethod(parameterType, bean, AFTER_REPAIR_METHOD);
        return bean;
    }

    /**
     * 调用补充的修复数据方法对当前对象进行额外数据修复处理
     * 调用自定义的方法对数据进行额外的处理
     *
     * @param parameterType 参数的类型
     * @param bean          对象的名字
     * @param methodName    方法名字
     */
    private void callUserDefinedMethod(Class<?> parameterType, Object bean, String methodName) {
        Method method = ReflectUtil.getMethodByName(parameterType, methodName);
        if (Objects.nonNull(method)) {
            ReflectUtil.invoke(bean, method);
        }
    }
    
    /**
     * 处理声明的字段
     *
     * @param obj           请求对象
     * @param parameterType 参数的类型
     * @param jsonFields    数据表的字段名字
     */
    private void dealDeclaredJsonFields(Object obj, Class<?> parameterType, Set<String> jsonFields) {
        Dict dict = Convert.convert(Dict.class, obj);
        Map<String, Map<String, Object>> jsonMergeFieldMap = new HashMap<>();
        for (Field field : parameterType.getDeclaredFields()) {
            GXMergeSingleField annotation = field.getAnnotation(GXMergeSingleField.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            String parentFieldName = annotation.parentFieldName();
            String fieldConfigName = annotation.fieldName();
            if (CharSequenceUtil.isBlank(fieldConfigName)) {
                fieldConfigName = field.getName();
            }
            String tableName = annotation.tableName();
            Object fieldDefaultValue = dict.get(field.getName());
            Class<? extends GXValidateJSONFieldService> service = annotation.service();
            Method method = ReflectUtil.getMethodByName(service, "getFieldValueByCondition");
            if (Objects.nonNull(method)) {
                GXValidateJSONFieldService validateJsonFieldService = GXSpringContextUtil.getBean(service);
                if (Objects.nonNull(validateJsonFieldService)) {
                    fieldDefaultValue = ReflectUtil.invoke(validateJsonFieldService, method, tableName, parentFieldName, fieldConfigName);
                }
            }
            jsonFields.add(parentFieldName);
            Object fieldValue = Optional.ofNullable(dict.getObj(CharSequenceUtil.toCamelCase(fieldConfigName))).orElse(fieldDefaultValue);
            Map<String, Object> tmpMap = new HashMap<>();
            if (CollUtil.contains(jsonMergeFieldMap.keySet(), parentFieldName)) {
                tmpMap = jsonMergeFieldMap.get(parentFieldName);
            }
            tmpMap.put(fieldConfigName, fieldValue);
            jsonMergeFieldMap.put(parentFieldName, tmpMap);
        }
        jsonMergeFieldMap.forEach((key, value) -> {
            Field field = ReflectUtil.getField(parameterType, key);
            if (Objects.nonNull(field)) {
                ReflectUtil.setFieldValue(obj, field, value);
            }
        });
    }

    /**
     * 获取请求的数据
     *
     * @param webRequest NativeWebRequest对象
     * @return String
     */
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        assert servletRequest != null;
        String jsonBody = (String) servletRequest.getAttribute(JSON_REQUEST_BODY);
        if (null == jsonBody) {
            try {
                jsonBody = IoUtil.read(servletRequest.getInputStream(), StandardCharsets.UTF_8);
                servletRequest.setAttribute(JSON_REQUEST_BODY, jsonBody);
            } catch (IOException e) {
                throw new GXBusinessException(e.getMessage(), e);
            }
        }
        if (!JSONUtil.isJson(jsonBody)) {
            throw new GXBusinessException(GXResultCode.REQUEST_JSON_NOT_BODY);
        }
        return jsonBody;
    }
}
