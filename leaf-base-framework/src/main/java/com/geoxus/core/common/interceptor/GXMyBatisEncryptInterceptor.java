package com.geoxus.core.common.interceptor;

import com.geoxus.core.common.annotation.GXSensitiveDataAnnotation;
import com.geoxus.core.common.service.GXSensitiveDataEncryptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Component
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class),
})
public class GXMyBatisEncryptInterceptor implements Interceptor {
    @Resource
    private GXSensitiveDataEncryptService gxSensitiveDataEncryptService;

    @Override
    @SuppressWarnings("all")
    public Object intercept(Invocation invocation) throws Throwable {
        // @Signature 指定了 type= parameterHandler 后，这里的 invocation.getTarget() 便是parameterHandler
        // 若指定ResultSetHandler ，这里则能强转为ResultSetHandler
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        // 获取参数对像，即 mapper 中 paramsType 的实例
        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        parameterField.setAccessible(true);
        // 取出实例
        Object parameterObject = parameterField.get(parameterHandler);
        if (parameterObject != null) {
            Class<?> parameterObjectClass = parameterObject.getClass();
            // 校验该实例的类是否被@SensitiveData所注解
            GXSensitiveDataAnnotation sensitiveData = AnnotationUtils.findAnnotation(parameterObjectClass, GXSensitiveDataAnnotation.class);
            if (Objects.nonNull(sensitiveData)) {
                // 取出当前当前类所有字段，传入加密方法
                Field[] declaredFields = parameterObjectClass.getDeclaredFields();
                // 对每个字段进行加密
                gxSensitiveDataEncryptService.encrypt(declaredFields, parameterObject);
            }
        }
        return invocation.proceed();
    }

    /**
     * 切记配置，否则当前拦截器不会加入拦截器链
     */
    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 自定义配置写入，没有自定义配置的可以直接置空此方法
    }
}
