package cn.maple.core.framework.config.aware;

import org.springframework.context.ApplicationContext;

public enum GXApplicationContextSingleton {
    INSTANCE;

    ApplicationContext applicationContext;

    GXApplicationContextSingleton() {
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}