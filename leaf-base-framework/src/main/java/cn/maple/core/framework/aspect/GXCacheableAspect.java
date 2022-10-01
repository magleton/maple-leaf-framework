package cn.maple.core.framework.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.annotation.GXCacheable;
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
import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class GXCacheableAspect {
    @Pointcut("@annotation(cn.maple.core.framework.annotation.GXCacheable)" + "|| @within(cn.maple.core.framework.annotation.GXCacheable)" + "|| target(cn.maple.core.datasource.service.GXMyBatisBaseService)")
    public void cacheablePointCut() {
        // 这是是切点标记
    }

    @Around("cacheablePointCut()")
    public Object around(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = point.getTarget().getClass();
        Object[] args = point.getArgs();
        GXCacheable cacheable = method.getAnnotation(GXCacheable.class);
        boolean evictCache = cacheable.evictCache();
        String cacheKey = CharSequenceUtil.lowerFirst(targetClass.getSimpleName()) + ":" + (CharSequenceUtil.isEmpty(cacheable.cacheKey()) ? method.getName() : cacheable.cacheKey());
        Object obtainData = GXCommonUtils.reflectCallObjectMethod(point.getTarget(), "getDataFromCache", cacheKey, args);
        if (Objects.nonNull(obtainData)) {
            return obtainData;
        }
        try {
            Object proceed;
            if (CollUtil.isNotEmpty(Arrays.asList(args))) {
                proceed = point.proceed(args);
            } else {
                proceed = point.proceed();
            }
            if (Objects.nonNull(proceed)) {
                if (evictCache) {
                    GXCommonUtils.reflectCallObjectMethod(point.getTarget(), "evictCacheData", cacheKey, args);
                }
                GXCommonUtils.reflectCallObjectMethod(point.getTarget(), "setCacheData", cacheKey, proceed, args);
            }
            return proceed;
        } catch (Throwable e) {
            throw new GXBusinessException(e.getMessage(), e);
        }
    }
}
