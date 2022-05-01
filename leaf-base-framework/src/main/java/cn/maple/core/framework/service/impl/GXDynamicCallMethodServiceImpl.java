package cn.maple.core.framework.service.impl;

import cn.maple.core.framework.service.GXDynamicCallMethodService;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GXDynamicCallMethodServiceImpl implements GXDynamicCallMethodService {
    @Override
    public Object call(String serviceClassName, String methodName, Object... parameters) {
        try {
            final Class<?> aClass = Class.forName(serviceClassName);
            final Object bean = GXSpringContextUtils.getBean(aClass);
            return call(bean, methodName, parameters);
        } catch (ClassNotFoundException e) {
            log.error("{}类不存在 , 异常信息 {}", serviceClassName, e.getMessage());
        }
        return null;
    }

    @Override
    public Object call(Object target, String methodName, Object... parameters) {
        return GXCommonUtils.reflectCallObjectMethod(target, methodName, parameters);
    }
}
