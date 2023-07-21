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

/**
 * 处理标注了DubboService的bean
 * 标注了DubboService的bean是Dubbo框架提的RPC API
 * 为了统一通用接口的名字 框架提供一些统一的接口
 * 当时这些接口需要有基础的服务来处理具体的业务逻辑
 * 所以产生了这个BeanPostProcessor类
 * 该BeanPostProcessor会将泛型中指定的具体业务服务类绑定
 * 当调用接口时会自动调用绑定的服务类来提供具体的功能
 */
@Component
@Log4j2
public class GXDubboRpcApiBeanPostProcessor implements BeanPostProcessor {
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
