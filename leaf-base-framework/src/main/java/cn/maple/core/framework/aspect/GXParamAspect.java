package cn.maple.core.framework.aspect;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.annotation.GXValidateRequestParam;
import cn.maple.core.framework.exception.GXBeanValidateException;
import cn.maple.core.framework.util.GXSpringContextUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.Set;

@Aspect
@Component
public class GXParamAspect {
    @Pointcut("@annotation(cn.maple.core.framework.annotation.GXValidateRequestParam)")
    public void requestParamRequire() {
        //标识切面的入口
    }

    @Around("requestParamRequire()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        final GXValidateRequestParam paramAnnotation = method.getAnnotation(GXValidateRequestParam.class);
        final boolean require = paramAnnotation.require();
        if (!require) {
            return point.proceed(point.getArgs());
        }
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<? extends ConstraintViolation<Object>> constraintViolations = validator.forExecutables().validateParameters(GXSpringContextUtils.getBean(method.getDeclaringClass()), method, point.getArgs());
        if (!constraintViolations.isEmpty()) {
            final Dict dict = Dict.create();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                final String currentFormName = constraint.getPropertyPath().toString();
                dict.set(currentFormName, constraint.getMessage());
            }
            throw new GXBeanValidateException("数据验证错误", HttpStatus.HTTP_INTERNAL_ERROR, dict);
        }
        return point.proceed(point.getArgs());
    }
}
