package cn.maple.core.framework.config.aware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    GXApplicationContextSingleton() {
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        LOG.info("GXApplicationContextSingleton类设置ApplicationContext对象被调用");
        if (Objects.isNull(this.applicationContext)) {
            this.applicationContext = applicationContext;
        }
    }
}