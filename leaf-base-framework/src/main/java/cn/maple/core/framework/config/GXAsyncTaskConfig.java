package cn.maple.core.framework.config;

import cn.maple.core.framework.wrapper.mdc.GXMDCVirtualThreadTaskExecutor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 异步任务
 */
@Configuration
public class GXAsyncTaskConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        Thread.Builder.OfVirtual ofVirtual = Thread.ofVirtual().name("virtual-async#", 1);
        ThreadFactory factory = ofVirtual.factory();
        return new GXMDCVirtualThreadTaskExecutor(Executors.newThreadPerTaskExecutor(factory));
    }
}