package com.geoxus.core.datasource.aspect;

import com.geoxus.core.datasource.annotation.GXDataSourceAnnotation;
import com.geoxus.core.datasource.config.GXDynamicContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 线程数据源切换处理类
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GXDataSourceAnnotationAspect {
    @Pointcut("@annotation(com.geoxus.core.datasource.annotation.GXDataSourceAnnotation) || @within(com.geoxus.core.datasource.annotation.GXDataSourceAnnotation)")
    public void dataSourcePointCut() {
        // 这是是切点标记
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();
        GXDataSourceAnnotation targetDataSourceAnnotation = targetClass.getAnnotation(GXDataSourceAnnotation.class);
        GXDataSourceAnnotation methodDataSourceAnnotation = method.getAnnotation(GXDataSourceAnnotation.class);
        if (targetDataSourceAnnotation != null || methodDataSourceAnnotation != null) {
            String value;
            if (methodDataSourceAnnotation != null) {
                value = methodDataSourceAnnotation.value();
            } else {
                value = targetDataSourceAnnotation.value();
            }
            GXDynamicContextHolder.push(value);
            log.debug("{}线程设置的数据源是{}", Thread.currentThread().getName(), value);
        }
        try {
            return point.proceed();
        } finally {
            GXDynamicContextHolder.poll();
            log.debug("{}线程清除数据源", Thread.currentThread().getName());
        }
    }
}