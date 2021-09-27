package com.geoxus.core.common.aspect;

import com.geoxus.core.common.annotation.GXDataScope;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class GXDataScopeAspect {
    @Pointcut("@annotation(com.geoxus.core.common.annotation.GXDataScope)")
    public void dataScope() {
        //标识切面的入口
    }

    @Around("dataScope()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        final GXDataScope paramAnnotation = method.getAnnotation(GXDataScope.class);
        Object proceed = point.proceed(point.getArgs());
        return proceed;
    }
}
