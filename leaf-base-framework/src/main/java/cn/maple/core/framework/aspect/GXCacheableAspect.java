package cn.maple.core.framework.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.framework.annotation.GXCacheable;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class GXCacheableAspect {
    private static final StandardReflectionParameterNameDiscoverer parameterNameDiscover = new StandardReflectionParameterNameDiscoverer();

    @Pointcut("@annotation(cn.maple.core.framework.annotation.GXCacheable)" + "|| @within(cn.maple.core.framework.annotation.GXCacheable)")
    public void cacheablePointCut() {
        // 这是是切点标记
    }

    @Around("cacheablePointCut()")
    public Object around(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = parameterNameDiscover.getParameterNames(method);
        Class<?> targetClass = point.getTarget().getClass();
        Object[] args = point.getArgs();
        GXCacheable cacheable = method.getAnnotation(GXCacheable.class);
        Class<? extends GXBaseResDto> tClass = cacheable.retType();
        String methodName = cacheable.methodName();
        String cacheKey = parseCacheKey(parameterNames, targetClass, method, cacheable, args);
        Object obtainData = GXCommonUtils.reflectCallObjectMethod(point.getTarget(), "getDataFromCache", cacheKey, args);
        if (Objects.nonNull(obtainData)) {
            if (tClass.isAssignableFrom(GXBaseResDto.class)) {
                return obtainData;
            }
            return GXCommonUtils.convertSourceToTarget(obtainData, tClass, methodName, null, Dict.create());
        }
        try {
            Object proceed;
            if (CollUtil.isNotEmpty(Arrays.asList(args))) {
                proceed = point.proceed(args);
            } else {
                proceed = point.proceed();
            }
            if (Objects.nonNull(proceed)) {
                GXCommonUtils.reflectCallObjectMethod(point.getTarget(), "setCacheData", cacheKey, proceed, args);
            }
            return proceed;
        } catch (Throwable e) {
            throw new GXBusinessException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("all")
    private String parseCacheKey(String[] parameterNames, Class<?> targetClass, Method method, GXCacheable cacheable, Object[] args) {
        List<String> tmpLst = new ArrayList<>();
        String cacheKey = cacheable.cacheKey();
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
                    tmpLst.add(s);
                }
            } else {
                tmpLst.add(CharSequenceUtil.replace(expr, "'", ""));
            }
        }

        String retCacheKey = CharSequenceUtil.format("{}:{}", CharSequenceUtil.lowerFirst(targetClass.getSimpleName()), CharSequenceUtil.isEmpty(cacheKey) ? method.getName() : cacheKey);
        if (CollUtil.isNotEmpty(tmpLst)) {
            retCacheKey = CollUtil.join(tmpLst, ":");
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
}
