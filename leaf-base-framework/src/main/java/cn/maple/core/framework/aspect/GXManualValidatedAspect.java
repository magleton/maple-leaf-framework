package cn.maple.core.framework.aspect;

import cn.hutool.core.util.ReflectUtil;
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

@Aspect
@Component
public class GXManualValidatedAspect {
    @Pointcut("@annotation(cn.maple.core.framework.annotation.GXManualValidated)")
    public void pointCut() {
        //标识切面的入口
    }

    @Before("pointCut()")
    public void before(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 0) {
            Arrays.stream(parameters).forEach(o -> {
                Method customizeProcess = ReflectUtil.getMethodByName(o.getClass(), "customizeProcess");
                if (Objects.nonNull(customizeProcess)) {
                    ReflectUtil.invoke(o, customizeProcess);
                }
            });
        }
    }
}
