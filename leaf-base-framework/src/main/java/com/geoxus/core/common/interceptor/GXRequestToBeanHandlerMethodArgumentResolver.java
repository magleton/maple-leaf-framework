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
import com.geoxus.core.common.annotation.GXMergeSingleFieldToJSONFieldAnnotation;
import com.geoxus.core.common.annotation.GXRequestBodyToTargetAnnotation;
import com.geoxus.core.common.constant.GXCommonConstant;
import com.geoxus.core.common.dto.GXBaseDto;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.common.event.GXMethodArgumentResolverEvent;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.mapstruct.GXBaseMapStruct;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.core.common.validator.impl.GXValidatorUtils;
import com.geoxus.core.common.vo.common.GXResultCode;
import com.geoxus.core.framework.service.GXCoreModelAttributesService;
import org.slf4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class GXRequestToBeanHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @GXFieldCommentAnnotation(zhDesc = "请求中的参数名字")
    public static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    @GXFieldCommentAnnotation("日志对象")
    private static final Logger LOGGER = GXCommonUtils.getLogger(GXRequestToBeanHandlerMethodArgumentResolver.class);

    @Resource
    private GXCoreModelAttributesService coreModelAttributesService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(GXRequestBodyToTargetAnnotation.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String body = getRequestBody(webRequest);
        final Dict dict = Convert.convert(Dict.class, JSONUtil.toBean(body, Dict.class));
        final Class<?> parameterType = parameter.getParameterType();
        Integer coreModelId;
        try {
            String upperCase = GXCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME.toUpperCase(Locale.ROOT);
            final Field coreModelIdField = parameterType.getDeclaredField(upperCase);
            coreModelId = Convert.toInt(ReflectUtil.getFieldValue(null, coreModelIdField));
        } catch (NoSuchFieldException e) {
            throw new GXException(CharSequenceUtil.format("{}类中请添加CORE_MODEL_ID静态常量字段", parameterType.getSimpleName()));
        }
        final GXRequestBodyToTargetAnnotation requestBodyToTargetAnnotation = parameter.getParameterAnnotation(GXRequestBodyToTargetAnnotation.class);
        final String value = Objects.requireNonNull(requestBodyToTargetAnnotation).value();
        Set<String> jsonFields = new HashSet<>(16);
        boolean fillJSONField = requestBodyToTargetAnnotation.fillJSONField();
        boolean validateTarget = requestBodyToTargetAnnotation.validateTarget();
        boolean validateCoreModelId = requestBodyToTargetAnnotation.validateCoreModelId();

        Map<String, Map<String, Object>> jsonMergeFieldMap = new HashMap<>();
        dealDeclaredFields(dict, parameterType, jsonFields, jsonMergeFieldMap);

        for (String jsonField : jsonFields) {
            dict.set(jsonField, JSONUtil.toJsonStr(jsonMergeFieldMap.get(jsonField)));
        }

        if (validateCoreModelId && null != coreModelId) {
            dealJsonFields(dict, coreModelId, jsonFields, fillJSONField);
        }

        Object bean = Convert.convert(parameterType, dict);
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
     * 处理JSON字段信息
     *
     * @param dict
     * @param coreModelId
     * @param jsonFields
     * @param fillJSONField
     */
    private void dealJsonFields(Dict dict, Integer coreModelId, Set<String> jsonFields, boolean fillJSONField) {
        for (String jsonField : jsonFields) {
            final String json = Optional.ofNullable(dict.getStr(jsonField)).orElse("{}");
            final Dict dbFieldDict = coreModelAttributesService.getModelAttributesDefaultValue(coreModelId, jsonField, json);
            Dict oDict = JSONUtil.toBean(json, Dict.class);
            Dict tmpDict = Dict.create();
            oDict.forEach((key, val) -> tmpDict.set(CharSequenceUtil.toUnderlineCase(key), val));
            GXCommonUtils.publishEvent(new GXMethodArgumentResolverEvent<>(tmpDict, dbFieldDict, "", Dict.create(), ""));
            final Set<String> tmpDictKey = tmpDict.keySet();
            if (!tmpDict.isEmpty() && !CollUtil.containsAll(dbFieldDict.keySet(), tmpDictKey)) {
                throw new GXException(CharSequenceUtil.format("{}字段参数不匹配(系统预置: {} , 实际请求: {})", jsonField, dbFieldDict.keySet(), tmpDictKey), GXResultCode.PARSE_REQUEST_JSON_ERROR.getCode());
            }
            if (fillJSONField && !dbFieldDict.isEmpty()) {
                dict.set(jsonField, JSONUtil.toJsonStr(dbFieldDict));
            }
        }
    }

    /**
     * 处理声明的字段
     *
     * @param dict
     * @param parameterType
     * @param jsonFields
     * @param jsonMergeFieldMap
     */
    private void dealDeclaredFields(Dict dict, Class<?> parameterType, Set<String> jsonFields, Map<String, Map<String, Object>> jsonMergeFieldMap) {
        for (Field field : parameterType.getDeclaredFields()) {
            GXMergeSingleFieldToJSONFieldAnnotation annotation = field.getAnnotation(GXMergeSingleFieldToJSONFieldAnnotation.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            String dbJSONFieldName = annotation.dbJSONFieldName();
            String dbFieldName = annotation.dbFieldName();
            if (CharSequenceUtil.isBlank(dbFieldName)) {
                dbFieldName = field.getName();
            }
            jsonFields.add(dbJSONFieldName);
            String currentFieldName = field.getName();
            Object fieldValue = Optional.ofNullable(dict.get(currentFieldName)).orElse(dict.getObj(CharSequenceUtil.toUnderlineCase(dbFieldName)));

            Map<String, Object> tmpMap = new HashMap<>();
            if (CollUtil.contains(jsonMergeFieldMap.keySet(), dbJSONFieldName)) {
                tmpMap = jsonMergeFieldMap.get(dbJSONFieldName);
            }
            tmpMap.put(dbFieldName, fieldValue);
            jsonMergeFieldMap.put(dbJSONFieldName, tmpMap);
        }
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
