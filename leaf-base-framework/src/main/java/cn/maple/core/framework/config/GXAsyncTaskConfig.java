package cn.maple.core.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务
 */
@Configuration
public class GXAsyncTaskConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        // 设置线程池关闭的时候需要等待所有任务都完成 才能销毁其他的Bean
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置核心池大小
        poolTaskExecutor.setCorePoolSize(1);
        // 设置最大池大小，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        poolTaskExecutor.setMaxPoolSize(2);
        // 设置队列容量
        poolTaskExecutor.setQueueCapacity(1000);
        // 设置保持活动秒数，当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
        poolTaskExecutor.setKeepAliveSeconds(120);
        // 设置线程名称前缀
        poolTaskExecutor.setThreadNamePrefix("async-task-thread-pool-");
        // 设置拒绝的执行处理策略
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化线程池
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }
}
