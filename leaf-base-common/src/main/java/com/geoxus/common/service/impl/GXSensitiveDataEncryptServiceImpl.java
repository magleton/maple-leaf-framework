package com.geoxus.common.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.common.annotation.GXSensitiveField;
import com.geoxus.common.service.GXSensitiveDataEncryptService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Objects;

@Service
public class GXSensitiveDataEncryptServiceImpl implements GXSensitiveDataEncryptService {
    @Override
    public <T> T encrypt(Field[] declaredFields, T paramsObject) throws IllegalAccessException {
        for (Field field : declaredFields) {
            GXSensitiveField sensitiveField = field.getAnnotation(GXSensitiveField.class);
            if (Objects.nonNull(sensitiveField)) {
                final Field accessible = ReflectUtil.setAccessible(field);
                Object object = accessible.get(paramsObject);
                // 只实现String类型的加密
                if (object instanceof String) {
                    final Class<?> serviceClazz = sensitiveField.serviceClazz();
                    final String encryptAlgorithm = sensitiveField.encryptAlgorithm();
                    final String deEncryptKey = sensitiveField.deEncryptKey();
                    final String[] params = sensitiveField.params();
                    String value = (String) object;
                    final Object bean = GXSpringContextUtil.getBean(serviceClazz);
                    final Object invoke = ReflectUtil.invoke(bean, encryptAlgorithm, value, deEncryptKey, params);
                    ReflectUtil.setFieldValue(paramsObject, accessible, invoke);
                }
            }
        }
        return paramsObject;
    }
}
