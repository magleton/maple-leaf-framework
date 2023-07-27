package cn.maple.redisson.processor;

import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.redisson.listener.GXRedissonMQListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * 处理继承了GXRedisPubSubListenerService的bean
 * 继承了GXRedisPubSubListenerService的bean是需要处理Redisson的pub/sub
 * 为了统一通用接口的名字 框架提供一些统一的接口
 * 当时这些接口需要处理redisson的pub/sub时
 * 只需要实现这个接口即可
 */
@Component
@Log4j2
@Lazy
public class GXRedissonMQListenerServicePostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        String targetSimpleName = GXRedissonMQListener.class.getSimpleName();
        Class<?>[] allInterfacesForClass = ClassUtils.getAllInterfacesForClass(bean.getClass());
        for (Class<?> interfacesForClass : allInterfacesForClass) {
            if (interfacesForClass.getSimpleName().equals(targetSimpleName)) {
                GXCommonUtils.reflectCallObjectMethod(bean, "registerRedissonListener");
                log.info("注册Redisson的PUB/SUB监听器 : {}", bean.getClass().getSimpleName());
            }
        }
        return bean;
    }
}