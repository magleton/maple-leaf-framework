package com.geoxus.common.aspect;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
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
import java.lang.reflect.Parameter;

/**
 * @author britton@126.com
 * @since 2021-10-19 15:23
 * <p>
 * 测量一个方法运行的时间
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GXStopWatchAspect {
    @Pointcut("@annotation(com.geoxus.common.annotation.GXStopWatch) || @within(com.geoxus.common.annotation.GXStopWatch)")
    public void stopWatchPointCut() {
        // 这只是切点标记
    }

    @Around("stopWatchPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        String classCanonicalName = targetClass.getCanonicalName();
        Method method = signature.getMethod();
        String name = method.getName();
        long end = System.currentTimeMillis();
        long last = (end - start) / 1000;
        Dict parametersDict = Dict.create();
        Parameter[] parameters = method.getParameters();
        int length = parameters.length;
        Object[] args = point.getArgs();
        for (int i = 0; i < length; i++) {
            String key = parameters[i].getName();
            Object realParam = args[i];
            parametersDict.set(key, realParam);
        }
        String callInfo = CharSequenceUtil.format("{}.{}", classCanonicalName, name);
        String threadName = Thread.currentThread().getName();
        log.info("{} : {}请求参数{}", threadName, callInfo, JSONUtil.toJsonStr(parametersDict));
        Object o = point.proceed();
        log.info("{} : {}响应数据{}", threadName, callInfo, JSONUtil.toJsonStr(o));
        log.info("{} : 本次调用{}方法总共运行{}秒", threadName, callInfo, last);
        return o;
    }
}
