package cn.maple.core.datasource.aspect;

import cn.hutool.core.collection.CollUtil;
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
public class GXCacheEvictAspect {
    @Pointcut("@annotation(cn.maple.core.datasource.annotation.GXCacheEvict) " + "|| @within(cn.maple.core.datasource.annotation.GXCacheEvict)")
    public void evictCachePointCut() {
        // 这是是切点标记
    }

    @Around("evictCachePointCut()")
    public Object around(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();
        try {
            Object proceed;
            if (CollUtil.isNotEmpty(Arrays.asList(args))) {
                proceed = point.proceed(args);
            } else {
                proceed = point.proceed();
            }
            if (Objects.nonNull(proceed)) {
                GXCommonUtils.reflectCallObjectMethod(point.getTarget(), "invalidateCacheData", method.getName(), args);
            }
            return proceed;
        } catch (Throwable e) {
            throw new GXBusinessException(e.getMessage(), e);
        }
    }
}
