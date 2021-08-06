package com.geoxus.core.common.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.annotation.GXMergeSingleFieldToJSONFieldAnnotation;
import com.geoxus.core.common.annotation.GXRequestBodyToTargetAnnotation;
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

import static cn.hutool.core.map.MapUtil.filter;

@Component
public class GXRequestToBeanHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @GXFieldCommentAnnotation(zhDesc = "请求中的参数名字")
    public static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    @GXFieldCommentAnnotation("日志对象")
    private static final Logger LOGGER = GXCommonUtils.getLogger(GXRequestToBeanHandlerMethodArgumentResolver.class);

    @Resource
    private GXCoreModelAttributesService gxCoreModelAttributesService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(GXRequestBodyToTargetAnnotation.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String body = getRequestBody(webRequest);
        final Dict dict = Convert.convert(Dict.class, JSONUtil.toBean(body, Dict.class));
        final Class<?> parameterType = parameter.getParameterType();
        Integer coreModelId = null;
        try {
            final Field coreModelIdField = parameterType.getDeclaredField("CORE_MODEL_ID");
            coreModelIdField.setAccessible(true);
            coreModelId = Convert.toInt(coreModelIdField.get(null));
        } catch (NoSuchFieldException e) {
            throw new GXException(CharSequenceUtil.format("{}类中请添加CORE_MODEL_ID静态常量字段", parameterType.getSimpleName()));
        }
        final GXRequestBodyToTargetAnnotation gxRequestBodyToTargetAnnotation = parameter.getParameterAnnotation(GXRequestBodyToTargetAnnotation.class);
        final String value = Objects.requireNonNull(gxRequestBodyToTargetAnnotation).value();
        List<String> jsonFields = new ArrayList<>(16);
        boolean fillJSONField = gxRequestBodyToTargetAnnotation.fillJSONField();
        boolean validateTarget = gxRequestBodyToTargetAnnotation.validateTarget();
        boolean validateCoreModelId = gxRequestBodyToTargetAnnotation.validateCoreModelId();
     /*   if (null == dict.getInt(GXCommonConstants.CORE_MODEL_PRIMARY_NAME) && validateCoreModelId) {
            throw new GXException(CharSequenceUtil.format("请传递{}参数", GXCommonConstants.CORE_MODEL_PRIMARY_NAME));
        }*/

        Map<String, Map<String, Object>> jsonMergeFieldMap = new HashMap<>();
        for (Field field : parameterType.getDeclaredFields()) {
            GXMergeSingleFieldToJSONFieldAnnotation annotation = field.getAnnotation(GXMergeSingleFieldToJSONFieldAnnotation.class);
            if (annotation == null) {
                continue;
            }
            String dbJSONFieldName = annotation.dbJSONFieldName();
            String dbFieldName = annotation.dbFieldName();
            if (CharSequenceUtil.isBlank(dbFieldName)) {
                dbFieldName = field.getName();
            }
            jsonFields.add(dbJSONFieldName);
            String currentFieldName = field.getName();
            Object fieldValue = dict.get(currentFieldName);
            if (Objects.isNull(fieldValue)) {
                fieldValue = dict.getObj(CharSequenceUtil.toSymbolCase(dbFieldName, '_'));
                if (Objects.isNull(fieldValue)) {
                    fieldValue = GXCommonUtils.getClassDefaultValue(field.getType());
                }
            }
            Map<String, Object> tmpMap = new HashMap<>();
            if (CollUtil.contains(jsonMergeFieldMap.keySet(), dbJSONFieldName)) {
                tmpMap = jsonMergeFieldMap.get(dbJSONFieldName);
            }
            tmpMap.put(dbFieldName, fieldValue);
            jsonMergeFieldMap.put(dbJSONFieldName, tmpMap);
        }

        for (String jsonField : jsonFields) {
            dict.set(jsonField, JSONUtil.toJsonStr(jsonMergeFieldMap.get(jsonField)));
        }

        //final Integer coreModelId = dict.getInt(GXCommonConstants.CORE_MODEL_PRIMARY_NAME);
        if (validateCoreModelId && null != coreModelId) {
            for (String jsonField : jsonFields) {
                final String json = Optional.ofNullable(dict.getStr(jsonField)).orElse("{}");
                final Dict dbFieldDict = gxCoreModelAttributesService.getModelAttributesDefaultValue(coreModelId, jsonField, json);
                Dict tmpDict = JSONUtil.toBean(json, Dict.class);
                GXCommonUtils.publishEvent(new GXMethodArgumentResolverEvent<>(tmpDict, dbFieldDict, "", Dict.create(), ""));
                final Set<String> tmpDictKey = tmpDict.keySet();
                if (!tmpDict.isEmpty() && !CollUtil.containsAll(dbFieldDict.keySet(), tmpDictKey)) {
                    throw new GXException(CharSequenceUtil.format("{}字段参数不匹配(系统预置: {} , 实际请求: {})", jsonField, dbFieldDict.keySet(), tmpDictKey), GXResultCode.PARSE_REQUEST_JSON_ERROR.getCode());
                }
                Map<String, Object> filter = filter(dbFieldDict, (Map.Entry<String, Object> t) -> null != tmpDict.getStr(t.getKey()));
                if (fillJSONField && !dbFieldDict.isEmpty()) {
                    dict.set(jsonField, JSONUtil.toJsonStr(dbFieldDict));
                } else if (!filter.isEmpty()) {
                    dict.set(jsonField, JSONUtil.toJsonStr(filter));
                }
            }
        }
        Object bean = Convert.convert(parameterType, dict);
        Class<?>[] groups = gxRequestBodyToTargetAnnotation.groups();
        if (validateTarget) {
            if (parameter.hasParameterAnnotation(Valid.class)) {
                GXValidatorUtils.validateEntity(bean, value, groups);
            } else if (parameter.hasParameterAnnotation(Validated.class)) {
                groups = Objects.requireNonNull(parameter.getParameterAnnotation(Validated.class)).value();
                GXValidatorUtils.validateEntity(bean, value, groups);
            }
        }

        Class<?> mapstructClazz = gxRequestBodyToTargetAnnotation.mapstructClazz();
        boolean isConvertToEntity = gxRequestBodyToTargetAnnotation.isConvertToEntity();
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
