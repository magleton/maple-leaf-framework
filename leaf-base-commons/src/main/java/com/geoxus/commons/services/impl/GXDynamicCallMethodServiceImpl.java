package com.geoxus.commons.services.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.geoxus.commons.services.GXDynamicCallMethodService;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.core.common.util.GXTypeOfUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class GXDynamicCallMethodServiceImpl implements GXDynamicCallMethodService {
    @Override
    public Object call(String serviceClassName, String methodName, Object parameters) {
        try {
            final Class<?> aClass = Class.forName(serviceClassName);
            final Method method = ReflectUtil.getMethodByName(aClass, methodName);
            final Object bean = GXSpringContextUtils.getBean(aClass);
            if (Objects.isNull(method) || Objects.isNull(bean)) {
                log.error("调用信息不正确{}#{}", serviceClassName, methodName);
                return null;
            }
            if (GXTypeOfUtils.typeof(parameters).getName().equalsIgnoreCase("java.util.ArrayList")) {
                final List<Object> parameterList = Convert.convert(new TypeReference<List<Object>>() {
                }, parameters);
                return method.invoke(bean, parameterList.toArray(new Object[0]));
            }
            return method.invoke(bean, parameters);
        } catch (ClassNotFoundException e) {
            log.error("{}类不存在 , 异常信息 {}", serviceClassName, e.getMessage());
        } catch (InvocationTargetException e) {
            log.error("反射调用失败 , 异常信息 : {} , Cause信息 : {}", JSONUtil.toJsonStr(e), JSONUtil.toJsonStr(e.getCause()));
        } catch (IllegalAccessException e) {
            log.error("无效的访问信息 , 异常信息 : {} , Cause信息 : {}", JSONUtil.toJsonStr(e), JSONUtil.toJsonStr(e.getCause()));
        }
        return null;
    }
}
