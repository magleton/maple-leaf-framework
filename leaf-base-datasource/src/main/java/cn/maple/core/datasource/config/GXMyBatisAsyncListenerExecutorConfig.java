package cn.maple.core.datasource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
public class GXMyBatisAsyncListenerExecutorConfig {
    @Bean("myBatisEventAsyncTaskExecutor")
    public AsyncTaskExecutor myBatisEventAsyncTaskExecutor() {
        Thread.Builder.OfVirtual ofVirtual = Thread.ofVirtual().name("virtual-mybatis-listener#", 1);
        ThreadFactory factory = ofVirtual.factory();
        return new TaskExecutorAdapter(Executors.newThreadPerTaskExecutor(factory));
    }
}