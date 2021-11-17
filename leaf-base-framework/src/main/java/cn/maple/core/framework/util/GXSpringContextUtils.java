package cn.maple.core.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

public class GXSpringContextUtils {
    private static final Logger LOG = LoggerFactory.getLogger(GXSpringContextUtils.class);

    private static final ApplicationContext applicationContext = GXApplicationContextSingleton.INSTANCE.getApplicationContext();

    private GXSpringContextUtils() {
    }

    public static Object getBean(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            LOG.warn(String.format("记录获取Bean的信息, 不影响业务, Bean获取出错 : %s / %s", clazz.getSimpleName(), e.getMessage()));
        }
        return null;
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        try {
            return applicationContext.getBean(name, requiredType);
        } catch (RuntimeException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }

    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    public static Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }

    public static void registerSingleton(String beanName, Object singletonObject) {
        if (null == getBean(singletonObject.getClass())) {
            ((AbstractApplicationContext) applicationContext).getBeanFactory().registerSingleton(beanName, singletonObject);
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private enum GXApplicationContextSingleton {
        INSTANCE;

        ApplicationContext applicationContext;

        GXApplicationContextSingleton() {
        }

        public ApplicationContext getApplicationContext() {
            return applicationContext;
        }

        private void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }
    }

    @Component
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @SuppressWarnings("all")
    private static class GXApplicationContextAware implements ApplicationContextAware {
        @Override
        public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
            GXApplicationContextSingleton.INSTANCE.setApplicationContext(applicationContext);
        }
    }
}
