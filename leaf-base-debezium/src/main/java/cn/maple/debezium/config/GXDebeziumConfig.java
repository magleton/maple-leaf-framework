package cn.maple.debezium.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@ConditionalOnClass(name = {"io.debezium.engine.DebeziumEngine"})
public class GXDebeziumConfig {
    @Bean("debeziumExecutor")
    public TaskExecutor debeziumExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 设置线程池关闭的时候需要等待所有任务都完成 才能销毁其他的Bean
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置核心池大小
        threadPoolTaskExecutor.setCorePoolSize(1);
        // 设置最大池大小，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        threadPoolTaskExecutor.setMaxPoolSize(2);
        // 设置队列容量
        threadPoolTaskExecutor.setQueueCapacity(1000);
        // 设置保持活动秒数，当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
        threadPoolTaskExecutor.setKeepAliveSeconds(120);
        // 设置线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("debezium-task-thread-pool-");
        // 设置拒绝的执行处理策略
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化线程池
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
