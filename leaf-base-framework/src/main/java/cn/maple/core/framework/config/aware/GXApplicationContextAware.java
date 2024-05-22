package cn.maple.core.framework.config.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@SuppressWarnings("all")
public class GXApplicationContextAware implements ApplicationContextAware, BeanFactoryPostProcessor {
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (GXApplicationContextSingleton.INSTANCE.getApplicationContext() == null) {
            GXApplicationContextSingleton.INSTANCE.setApplicationContext(applicationContext);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (GXApplicationContextSingleton.INSTANCE.getBeanFactory() == null) {
            GXApplicationContextSingleton.INSTANCE.setBeanFactory(beanFactory);
        }
    }
}