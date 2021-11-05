package cn.maple.core.framework.aspect;

import cn.maple.core.framework.annotation.GXValidatedParam;
import cn.maple.core.framework.util.GXValidatorUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class GXValidatedAspect {
    @Pointcut("@annotation(cn.maple.core.framework.annotation.GXValidated)")
    public void pointCut() {
        //标识切面的入口
    }

    @Before("pointCut()")
    public void before(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = point.getArgs();
        if (parameters.length > 0) {
            AtomicInteger index = new AtomicInteger();
            Arrays.stream(parameters).forEach(o -> {
                GXValidatedParam annotation = o.getAnnotation(GXValidatedParam.class);
                if (Objects.nonNull(annotation)) {
                    Class<?>[] groups = annotation.groups();
                    GXValidatorUtil.validateEntity(args[index.get()], groups);
                }
                index.getAndIncrement();
            });
        }
    }
}
