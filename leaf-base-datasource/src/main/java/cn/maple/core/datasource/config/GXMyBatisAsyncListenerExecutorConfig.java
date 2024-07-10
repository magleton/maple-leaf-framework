package cn.maple.core.datasource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class GXMyBatisAsyncListenerExecutorConfig {
    @Bean("myBatisEventAsyncTaskExecutor")
    public AsyncTaskExecutor myBatisEventAsyncTaskExecutor() {
        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        // 设置线程池关闭的时候需要等待所有任务都完成 才能销毁其他的Bean
        asyncTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置核心池大小
        asyncTaskExecutor.setCorePoolSize(1);
        // 设置最大池大小，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        asyncTaskExecutor.setMaxPoolSize(2);
        // 设置队列容量
        asyncTaskExecutor.setQueueCapacity(1000);
        // 设置保持活动秒数，当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
        asyncTaskExecutor.setKeepAliveSeconds(120);
        // 设置线程名称前缀
        asyncTaskExecutor.setThreadNamePrefix("mybatis-event-async-task-thread-pool-");
        // 设置拒绝的执行处理策略
        asyncTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化线程池
        asyncTaskExecutor.initialize();
        return asyncTaskExecutor;
    }
}