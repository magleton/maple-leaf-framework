package cn.maple.core.datasource.aspect;

import cn.maple.core.datasource.annotation.GXCacheable;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class GXCacheableAspect {
    @Pointcut("@annotation(cn.maple.core.datasource.annotation.GXCacheable)" + "|| @within(cn.maple.core.datasource.annotation.GXCacheable)" + "|| target(cn.maple.core.datasource.service.GXMyBatisBaseService)")
    public void cacheablePointCut() {
        // 这是是切点标记
    }

    @Around("cacheablePointCut()")
    public Object around(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();
        GXCacheable cacheable = targetClass.getAnnotation(GXCacheable.class);
        Object obtainData = GXCommonUtils.reflectCallObjectMethod(point.getTarget(), "getDataFromCache", method.getName(), args);
        if (Objects.nonNull(obtainData)) {
            return obtainData;
        }
        try {
            Object proceed = point.proceed();
            if (Objects.nonNull(proceed)) {
                GXCommonUtils.reflectCallObjectMethod(point.getTarget(), "setCacheData", method.getName(), proceed, args);
            }
            return proceed;
        } catch (Throwable e) {
            throw new GXBusinessException("设置缓存数据失败", e);
        }
    }
}
