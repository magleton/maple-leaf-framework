package com.geoxus.commons.aspect;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class GXStatisticsMethodExecutionTimeAspect {
    @Around("@annotation(com.geoxus.commons.annotation.GXStatisticsMethodExecutionTimeAnnotation)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //记录请求信息及时间到控制台
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        Object[] args = point.getArgs();
        final String threadName = Thread.currentThread().getName();
        log.info(StrUtil.format("花费时间:{} , 当前线程：{} , 请求类名字:{} , 请求方法：{} , 请求参数:{}", time, threadName, className, methodName, args));
        return result;
    }
}
