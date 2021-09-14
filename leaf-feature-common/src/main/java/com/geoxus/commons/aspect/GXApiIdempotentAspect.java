package com.geoxus.commons.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.geoxus.commons.annotation.GXApiIdempotentAnnotation;
import com.geoxus.core.common.util.GXResultUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class GXApiIdempotentAspect {
    @Pointcut("@annotation(com.geoxus.commons.annotation.GXApiIdempotentAnnotation)")
    public void apiIdempotentPointCut() {
        // 切入点表达式, 只是作为一个入口标示
    }

    @Around("apiIdempotentPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        final Method method = getMethod(point);
        final Object[] condition = getParameters(point);
        final Object targetObject = getTargetObject(point);
        if (Objects.isNull(targetObject) || Objects.isNull(method)) {
            Dict dict = Dict.create().set("error", CharSequenceUtil.format("验证对象信息不存在信息, 请核对是否配置正确~~~"));
            return GXResultUtils.error(dict);
        }
        final boolean retData = invoke(targetObject, method, condition);
        if (!retData) {
            Dict dict = Dict.create().set("error", CharSequenceUtil.format("请不要提交的太过于频繁~~~~"));
            return GXResultUtils.error(dict);
        }
        return point.proceed(point.getArgs());
    }

    /**
     * 将注解配置的字段信息和实际参数进行匹配
     * 并且形成调用函数的实际参数
     *
     * @param point 切点对象
     * @return 方法接受的参数列表
     */
    private Object[] getParameters(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        return point.getArgs();
    }

    /**
     * 从Spring容器中获取实际对用对象
     * 该对象必须是GXApiIdempotentService类型的对象
     *
     * @param point 切点对象
     * @return 实际调用对象
     */
    private Object getTargetObject(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method signatureMethod = signature.getMethod();
        final GXApiIdempotentAnnotation methodAnnotation = signatureMethod.getAnnotation(GXApiIdempotentAnnotation.class);
        final Class<?> service = methodAnnotation.service();
        return SpringUtil.getBean(service);
    }

    /**
     * 通过反射调用函数
     *
     * @param targetObject 目标对象
     * @param targetMethod 目标方法
     * @param condition    条件
     * @return 参数
     * @throws InvocationTargetException 异常对象
     * @throws IllegalAccessException    异常对象
     */
    private boolean invoke(Object targetObject, Method targetMethod, Object condition) throws InvocationTargetException, IllegalAccessException {
        final Object invoke = targetMethod.invoke(targetObject, condition);
        if (Objects.isNull(invoke)) {
            return Boolean.TRUE;
        }
        return Convert.convert(new TypeReference<Boolean>() {
        }, invoke);
    }

    /**
     * 从注解信息中获取实际方法
     *
     * @param point 切点对象
     * @return 被调用方法对象
     */
    private Method getMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method signatureMethod = signature.getMethod();
        final GXApiIdempotentAnnotation methodAnnotation = signatureMethod.getAnnotation(GXApiIdempotentAnnotation.class);
        final Class<?> service = methodAnnotation.service();
        final String methodName = methodAnnotation.methodName();
        return ReflectUtil.getMethodByNameIgnoreCase(SpringUtil.getBean(service).getClass(), methodName);
    }
}
