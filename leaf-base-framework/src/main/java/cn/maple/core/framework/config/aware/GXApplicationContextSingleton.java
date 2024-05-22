package cn.maple.core.framework.config.aware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.Objects;

@SuppressWarnings("all")
public enum GXApplicationContextSingleton {
    INSTANCE;

    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(GXApplicationContextSingleton.class);

    /**
     * Spring应用上下文环境
     */
    ApplicationContext applicationContext;

    /**
     * "@PostConstruct"注解标记的类中，由于ApplicationContext还未加载，导致空指针<br>
     * 因此实现BeanFactoryPostProcessor注入ConfigurableListableBeanFactory实现bean的操作
     */
    ConfigurableListableBeanFactory beanFactory;

    GXApplicationContextSingleton() {
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        LOG.info("GXApplicationContextSingleton类设置ApplicationContext对象被调用");
        if (Objects.isNull(this.applicationContext)) {
            this.applicationContext = applicationContext;
        }
    }

    public void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        LOG.info("GXApplicationContextSingleton类设置ConfigurableListableBeanFactory对象被调用");
        if (Objects.isNull(this.beanFactory)) {
            this.beanFactory = beanFactory;
        }
    }
}