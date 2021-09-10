package com.geoxus.core.common.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.annotation.GXRequestBodyToTargetAnnotation;
import com.geoxus.core.common.annotation.GXSingleFieldToDbJsonFieldAnnotation;
import com.geoxus.core.common.dto.GXBaseDto;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.mapstruct.GXBaseMapStruct;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.core.common.validator.GXValidateJsonFieldService;
import com.geoxus.core.common.validator.impl.GXValidatorUtils;
import com.geoxus.core.common.vo.common.GXResultCode;
import org.slf4j.Logger;
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
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class GXRequestToBeanHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @GXFieldCommentAnnotation(zhDesc = "请求中的参数名字")
    public static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    @GXFieldCommentAnnotation("日志对象")
    private static final Logger LOGGER = GXCommonUtils.getLogger(GXRequestToBeanHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(GXRequestBodyToTargetAnnotation.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String body = getRequestBody(webRequest);
        final Class<?> parameterType = parameter.getParameterType();
        final Object bean = Convert.convert(parameterType, JSONUtil.toBean(body, parameterType));
        final GXRequestBodyToTargetAnnotation requestBodyToTargetAnnotation = parameter.getParameterAnnotation(GXRequestBodyToTargetAnnotation.class);
        final String value = Objects.requireNonNull(requestBodyToTargetAnnotation).value();
        Set<String> jsonFields = new HashSet<>(16);
        boolean validateTarget = requestBodyToTargetAnnotation.validateTarget();

        callUserDefinedMethod(parameterType, bean);
        dealDeclaredJsonFields(bean, parameterType, jsonFields);
        Class<?>[] groups = requestBodyToTargetAnnotation.groups();
        if (validateTarget) {
            if (parameter.hasParameterAnnotation(Valid.class)) {
                GXValidatorUtils.validateEntity(bean, value, groups);
            } else if (parameter.hasParameterAnnotation(Validated.class)) {
                groups = Objects.requireNonNull(parameter.getParameterAnnotation(Validated.class)).value();
                GXValidatorUtils.validateEntity(bean, value, groups);
            }
        }

        Class<?> mapstructClazz = requestBodyToTargetAnnotation.mapstructClazz();
        boolean isConvertToEntity = requestBodyToTargetAnnotation.isConvertToEntity();
        if (mapstructClazz != Void.class && isConvertToEntity) {
            GXBaseMapStruct<GXBaseDto, GXBaseEntity> convert = Convert.convert(new TypeReference<GXBaseMapStruct<GXBaseDto, GXBaseEntity>>() {
            }, GXSpringContextUtils.getBean(mapstructClazz));
            if (null == convert) {
                LOGGER.error("DTO转换为Entity失败!请提供正确的MapStruct转换Class");
                return null;
            }
            return convert.dtoToEntity(Convert.convert((Type) parameterType, bean));
        }

        return bean;
    }

    /**
     * 调用补充的方法对当前对象进行额外的处理
     *
     * @param parameterType 参数的类型
     * @param bean          对象的名字
     */
    private void callUserDefinedMethod(Class<?> parameterType, Object bean) {
        Arrays.asList("verify", "repair").forEach(methodName -> {
            Method method = ReflectUtil.getMethodByName(parameterType, methodName);
            if (Objects.nonNull(method)) {
                ReflectUtil.invoke(bean, method);
            }
        });
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
            GXSingleFieldToDbJsonFieldAnnotation annotation = field.getAnnotation(GXSingleFieldToDbJsonFieldAnnotation.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            String dbJsonFieldName = annotation.dbJSONFieldName();
            String dbFieldName = annotation.dbFieldName();
            if (CharSequenceUtil.isBlank(dbFieldName)) {
                dbFieldName = field.getName();
            }
            String tableName = annotation.tableName();
            Object fieldDefaultValue = null;
            Class<? extends GXValidateJsonFieldService> service = annotation.service();
            Method method = ReflectUtil.getMethodByName(service, "getFieldValueByCondition");
            if (Objects.nonNull(method)) {
                GXValidateJsonFieldService bean = GXSpringContextUtils.getBean(service);
                fieldDefaultValue = ReflectUtil.invoke(bean, method, tableName, dbFieldName);
            }
            jsonFields.add(dbJsonFieldName);
            Object fieldValue = Optional.ofNullable(dict.getObj(CharSequenceUtil.toCamelCase(dbFieldName))).orElse(fieldDefaultValue);
            Map<String, Object> tmpMap = new HashMap<>();
            if (CollUtil.contains(jsonMergeFieldMap.keySet(), dbJsonFieldName)) {
                tmpMap = jsonMergeFieldMap.get(dbJsonFieldName);
            }
            tmpMap.put(dbFieldName, fieldValue);
            jsonMergeFieldMap.put(dbJsonFieldName, tmpMap);
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
                throw new GXException(e.getMessage(), e);
            }
        }
        if (!JSONUtil.isJson(jsonBody)) {
            throw new GXException(GXResultCode.REQUEST_JSON_NOT_BODY);
        }
        return jsonBody;
    }
}
