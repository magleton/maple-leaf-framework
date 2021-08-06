package com.geoxus.commons.services.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.geoxus.commons.dto.GXDynamicCallParamAttributeDto;
import com.geoxus.commons.dto.GXDynamicCallParamDto;
import com.geoxus.commons.services.GXParseDynamicCallParamService;
import com.geoxus.core.common.util.GXSpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class GXParseDynamicCallParamServiceImpl implements GXParseDynamicCallParamService {
    /**
     * 参数的值来自于回调方法
     *
     * @param callParamDto 参数信息配置
     * @return Object
     */
    private Object getValueFromCallback(GXDynamicCallParamAttributeDto callParamDto) {
        try {
            final String callBackClassName = callParamDto.getCallBackClassName();
            final Class<?> aClass = Class.forName(callBackClassName);
            final String callBackMethodName = callParamDto.getCallBackMethodName();
            final Object bean = GXSpringContextUtils.getBean(aClass);
            if (Objects.isNull(bean)) {
                log.error("callBackClassName = {}的bean不存在", callBackClassName);
                return null;
            }
            final Method method = ReflectUtil.getMethodByName(aClass, callBackMethodName);
            if (Objects.isNull(method)) {
                log.error("callBackMethodName = {}在bean中不存在", aClass);
                return null;
            }
            return method.invoke(bean);
        } catch (Exception e) {
            log.error("反射调用获取参数值失败 {}", JSONUtil.toJsonStr(e));
        }
        return null;
    }

    /**
     * 值来自于固定分配
     *
     * @param callParamDto 参数信息配置
     * @return Object
     */
    private Object getValueFromAssign(GXDynamicCallParamAttributeDto callParamDto) {
        return callParamDto.getFixedAssignedValue();
    }

    /**
     * 获取动态调用的方法的参数实参
     *
     * @param jsonStr JSON字符串
     * @return Object
     */
    @Override
    public Object getDynamicCallMethodParamValue(String jsonStr) {
        if (JSONUtil.isNull(jsonStr) || !JSONUtil.isJson(jsonStr)) {
            log.error("参数请传递JSON类型");
            return null;
        }
        final GXDynamicCallParamDto callParamDto = JSONUtil.toBean(jsonStr, GXDynamicCallParamDto.class);
        final String javaType = callParamDto.getJavaType();
        final List<GXDynamicCallParamAttributeDto> attributes = callParamDto.getAttributes();
        if (CharSequenceUtil.isBlank(javaType)) {
            return getParamValueList(attributes);
        }
        final Dict paramValueObject = getParamValueObject(attributes);
        Class<?> aClass = null;
        try {
            aClass = Class.forName(javaType);
            return JSONUtil.toBean(JSONUtil.toJsonStr(paramValueObject), aClass);
        } catch (Exception e) {
            log.error("{}转换为{}失败", JSONUtil.toJsonStr(paramValueObject), aClass);
        }
        return null;
    }

    /**
     * 使用单个值调用服务方法
     * eg:
     * XXXService.xxMethod(String name , Integer age);
     *
     * @param callParamAttributes 参数值
     * @return List
     */
    private List<Object> getParamValueList(List<GXDynamicCallParamAttributeDto> callParamAttributes) {
        final ArrayList<Object> objects = new ArrayList<>();
        callParamAttributes.forEach(attribute -> {
            final String dataSource = attribute.getDataSource();
            Object value = null;
            if (CharSequenceUtil.equalsIgnoreCase(dataSource, "token")) {
                value = getValueFromToken(attribute);
            } else if (CharSequenceUtil.equalsIgnoreCase(dataSource, "assign")) {
                value = getValueFromAssign(attribute);
            } else if (CharSequenceUtil.equalsIgnoreCase(dataSource, "callback")) {
                value = getValueFromCallback(attribute);
            }
            objects.add(value);
        });
        return objects;
    }

    /**
     * 使用对象调用服务方法
     * eg:
     * XXXService.xxMethod(TestReqDto testReqDto);
     *
     * @param callParamAttributes 参数值
     * @return List
     */
    private Dict getParamValueObject(List<GXDynamicCallParamAttributeDto> callParamAttributes) {
        final Dict dict = Dict.create();
        callParamAttributes.forEach(attribute -> {
            final String fieldName = attribute.getFieldName();
            final String dataSource = attribute.getDataSource();
            Object value = null;
            if (CharSequenceUtil.equalsIgnoreCase(dataSource, "token")) {
                value = getValueFromToken(attribute);
            } else if (CharSequenceUtil.equalsIgnoreCase(dataSource, "assign")) {
                value = getValueFromAssign(attribute);
            } else if (CharSequenceUtil.equalsIgnoreCase(dataSource, "callback")) {
                value = getValueFromCallback(attribute);
            }
            dict.set(fieldName, value);
        });
        return dict;
    }

    /**
     * 值来自于token中指定的字段
     *
     * @param callParamDto 参数信息配置
     * @return Object
     */
    private Object getValueFromToken(GXDynamicCallParamAttributeDto callParamDto) {
        final Dict data = Dict.create();
        final String sourceFieldName = callParamDto.getSourceFieldName();
        return data.getObj(sourceFieldName);
    }
}
