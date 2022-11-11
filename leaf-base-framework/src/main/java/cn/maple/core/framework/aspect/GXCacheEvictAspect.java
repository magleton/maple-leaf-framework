package cn.maple.core.framework.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.framework.annotation.GXCacheEvict;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.service.GXBaseCacheLockService;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

@Aspect
@Component
@Slf4j
public class GXCacheEvictAspect {
    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscover = new LocalVariableTableParameterNameDiscoverer();

    @Pointcut("@annotation(cn.maple.core.framework.annotation.GXCacheEvict) " + "|| @within(cn.maple.core.framework.annotation.GXCacheEvict)")
    public void evictCachePointCut() {
        // 这是是切点标记
    }

    @Around("evictCachePointCut()")
    public Object around(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = parameterNameDiscover.getParameterNames(method);
        Class<?> targetClass = point.getTarget().getClass();
        Object[] args = point.getArgs();
        GXCacheEvict cacheEvict = method.getAnnotation(GXCacheEvict.class);
        String cacheKey = parseCacheKey(parameterNames, targetClass, method, cacheEvict, args);
        try {
            Object proceed;
            if (CollUtil.isNotEmpty(Arrays.asList(args))) {
                proceed = point.proceed(args);
            } else {
                proceed = point.proceed();
            }
            if (Objects.nonNull(proceed)) {
                Lock cacheLock = getCacheLock(cacheKey);
                try {
                    cacheLock.lock();
                    GXCommonUtils.reflectCallObjectMethod(point.getTarget(), "evictCacheData", cacheKey, args);
                } finally {
                    cacheLock.unlock();
                }
            }
            return proceed;
        } catch (Throwable e) {
            throw new GXBusinessException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("all")
    private String parseCacheKey(String[] parameterNames, Class<?> targetClass, Method method, GXCacheEvict cacheEvict, Object[] args) {
        List<String> tmpValues = new ArrayList<>();
        String cacheKey = cacheEvict.cacheKey();
        List<String> expressions = CharSequenceUtil.split(cacheKey, '+');

        for (String expr : expressions) {
            expr = CharSequenceUtil.trim(expr);
            if (CharSequenceUtil.isEmpty(expr)) {
                continue;
            }
            if (CharSequenceUtil.startWith(expr, "#")) {
                // 处理表达式 表达式跟参数有关
                String s = dealParam(expr, parameterNames, args);
                if (CharSequenceUtil.isNotEmpty(s)) {
                    tmpValues.add(s);
                }
            } else {
                tmpValues.add(CharSequenceUtil.replace(expr, "'", ""));
            }
        }

        String retCacheKey = CharSequenceUtil.format("{}:{}", CharSequenceUtil.lowerFirst(targetClass.getSimpleName()), CharSequenceUtil.isEmpty(cacheKey) ? method.getName() : cacheKey);
        if (CollUtil.isNotEmpty(tmpValues)) {
            retCacheKey = CollUtil.join(tmpValues, ":");
        }
        return retCacheKey;
    }

    @SuppressWarnings("all")
    private String dealParam(String expr, String[] parameterNames, Object[] args) {
        String targetParamName = CharSequenceUtil.replace(expr, "#", "");
        String getMethodName = "";
        if (CharSequenceUtil.contains(targetParamName, '.')) {
            List<String> split = CharSequenceUtil.split(targetParamName, '.', 2);
            targetParamName = split.get(0);
            getMethodName = CharSequenceUtil.format("get{}", CharSequenceUtil.upperFirst(split.get(1)));
        }
        int length = parameterNames.length;
        for (int index = 0; index < length; index++) {
            if (CharSequenceUtil.equals(targetParamName, parameterNames[index])) {
                Object arg = args[index];
                if (CharSequenceUtil.isNotEmpty(getMethodName)) {
                    Method method = ReflectUtil.getMethod(arg.getClass(), getMethodName);
                    if (Objects.isNull(method)) {
                        throw new GXBusinessException("Leaf框架需要的缓存方法不存在!");
                    }
                    Object o = GXCommonUtils.reflectCallObjectMethod(arg, getMethodName);
                    return Objects.isNull(o) ? null : o.toString();
                } else {
                    return arg.toString();
                }
            }
        }
        return null;
    }

    /**
     * 获取缓存锁
     *
     * @param lockName 锁的名字
     * @return Lock 锁对象
     */
    @SuppressWarnings("all")
    private Lock getCacheLock(String lockName) {
        return Objects.requireNonNull(GXSpringContextUtils.getBean(GXBaseCacheLockService.class)).getLock(lockName);
    }
}