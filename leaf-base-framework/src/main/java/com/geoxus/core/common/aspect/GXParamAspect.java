package com.geoxus.core.common.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.geoxus.core.common.annotation.GXParamAnnotation;
import com.geoxus.core.common.exception.GXException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
public class GXParamAspect {
    @Pointcut("@annotation(com.geoxus.core.common.annotation.GXParamAnnotation)")
    public void requestParamRequire() {
        //标识切面的入口
    }

    @Around("requestParamRequire()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        final GXParamAnnotation paramAnnotation = method.getAnnotation(GXParamAnnotation.class);
        final String[] paramNames = paramAnnotation.paramNames();
        final boolean require = paramAnnotation.require();
        if (!require) {
            return point.proceed(point.getArgs());
        }
        final Object requestParam = point.getArgs()[0];
        if (requestParam instanceof Map) {
            final Dict dict = Convert.convert(Dict.class, requestParam);
            final Set<String> keySet = dict.keySet();
            if (!CollUtil.containsAll(keySet, Arrays.asList(paramNames))) {
                throw new GXException(StrUtil.format("参数{}必填", String.join(",", paramNames)));
            }
        }
        return point.proceed(point.getArgs());
    }
}
