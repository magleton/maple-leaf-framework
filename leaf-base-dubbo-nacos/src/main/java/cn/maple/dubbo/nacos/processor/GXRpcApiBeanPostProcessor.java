package cn.maple.dubbo.nacos.processor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class GXRpcApiBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        String className = bean.getClass().getName();
        if (CharSequenceUtil.equals(className, ServiceBean.class.getName())) {
            Object realRpcApiTarget = ((ServiceBean<?>) bean).getRef();
            Class<?> targetService = GXCommonUtils.getGenericClassType(realRpcApiTarget.getClass(), 0);
            if (ObjectUtil.isNull(targetService)) {
                return bean;
            }
            Object targetServiceObject = GXSpringContextUtils.getBean(targetService);
            if (ObjectUtil.isNotNull(targetServiceObject)) {
                GXCommonUtils.reflectCallObjectMethod(realRpcApiTarget, "staticBindServeServiceClass", targetService);
                log.info("DUBBO RPC API: 《{}》使用的底层服务是《{}》", realRpcApiTarget.getClass().getSimpleName(), targetService.getSimpleName());
            }
        }
        return bean;
    }
}
