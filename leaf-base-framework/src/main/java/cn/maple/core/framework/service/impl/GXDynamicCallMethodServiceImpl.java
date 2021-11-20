package cn.maple.core.framework.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.maple.core.framework.service.GXDynamicCallMethodService;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.core.framework.util.GXTypeOfUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GXDynamicCallMethodServiceImpl implements GXDynamicCallMethodService {
    @Override
    public Object call(String serviceClassName, String methodName, Object parameters) {
        try {
            final Class<?> aClass = Class.forName(serviceClassName);
            final Object bean = GXSpringContextUtils.getBean(aClass);
            if (GXTypeOfUtils.typeof(parameters).getName().equalsIgnoreCase("java.util.ArrayList")) {
                final List<Object> parameterList = Convert.convert(new TypeReference<>() {
                }, parameters);
                return call(bean, methodName, parameterList.toArray(new Object[0]));
            }
            return call(bean, methodName, parameters);
        } catch (ClassNotFoundException e) {
            log.error("{}类不存在 , 异常信息 {}", serviceClassName, e.getMessage());
        }
        return null;
    }

    @Override
    public Object call(Object target, String methodName, Object parameters) {
        return GXCommonUtils.reflectCallObjectMethod(target, methodName, parameters);
    }
}
