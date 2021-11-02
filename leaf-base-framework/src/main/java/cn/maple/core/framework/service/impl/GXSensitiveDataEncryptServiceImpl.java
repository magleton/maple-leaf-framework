package cn.maple.core.framework.service.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.core.framework.annotation.GXSensitiveField;
import cn.maple.core.framework.service.GXSensitiveDataEncryptService;
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
                    final Object bean = GXSpringContextUtils.getBean(serviceClazz);
                    final Object invoke = ReflectUtil.invoke(bean, encryptAlgorithm, value, deEncryptKey, params);
                    ReflectUtil.setFieldValue(paramsObject, accessible, invoke);
                }
            }
        }
        return paramsObject;
    }
}
