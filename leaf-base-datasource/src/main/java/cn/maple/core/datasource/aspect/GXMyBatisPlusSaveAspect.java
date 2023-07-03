package cn.maple.core.datasource.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GXMyBatisPlusSaveAspect {
    @Around("execution(* com.baomidou.mybatisplus.extension.service.IService.save(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("发布新增前的事件,服务可以通过监听该事件对目标对象进行新增前的最后处理");
        Object proceed = point.proceed();
        log.info("发布新增后的事件,服务可以通过监听该事件对目标对象进行新增后的最后处理");
        return proceed;
    }
}
