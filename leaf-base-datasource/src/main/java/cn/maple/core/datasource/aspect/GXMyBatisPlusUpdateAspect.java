package cn.maple.core.datasource.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GXMyBatisPlusUpdateAspect {
    @Around("execution(* cn.maple.core.datasource.mapper.GXBaseMapper.update(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("发布更新前的事件,服务可以通过监听该事件对目标对象进行更新前的最后处理");
        Object proceed = point.proceed();
        log.info("发布更新后的事件,服务可以通过监听该事件对目标对象进行更新后的最后处理");
        return proceed;
    }
}
