package cn.maple.core.framework.event.config;

import cn.maple.core.framework.config.aware.GXApplicationContextSingleton;
import cn.maple.core.framework.util.GXCommonUtils;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.Executor;

@Configuration
@Log4j2
@Profile("guava")
public class GuavaEventBusConfig implements ApplicationContextAware {
    /**
     * 定义同步事件总线
     *
     * @return EventBus 同步事件总线
     */
    @Bean("eventBus")
    public EventBus eventBus() {
        log.info("初始化Guava EventBus的同步事件对象");
        return new EventBus("maple-event-bus");
    }

    /**
     * 定义异步事件总线
     *
     * @return AsyncEventBus 异步事件总线
     */
    @Bean("asyncEventBus")
    public AsyncEventBus asyncEventBus() {
        log.info("初始化Guava EventBus的异步事件对象");
        String identifier = GXCommonUtils.getEnvironmentValue("spring.application.name", String.class, "maple-async-event-bus");
        Executor executor = MoreExecutors.directExecutor();
        return new AsyncEventBus(identifier, executor);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        GXApplicationContextSingleton.INSTANCE.setApplicationContext(applicationContext);
    }
}
