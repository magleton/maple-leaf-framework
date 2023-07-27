package cn.maple.redisson.processor;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.maple.redisson.listener.GXRedissonDelayedQueueListener;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 处理实现了GXRedisDelayedQueueListener这个监听器接口的bean
 */
@Component
@Log4j2
public class GXRedissonDelayQueueListenerPostProcessor implements BeanPostProcessor {
    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(GXRedissonDelayQueueListenerPostProcessor.class);

    /**
     * 执行延迟队列任务的线程池
     */
    private static final ExecutorService DELAY_QUEUE_EXECUTOR = ExecutorBuilder.create()
            .setCorePoolSize(2)
            .setMaxPoolSize(10)
            .setThreadFactory(new NamedThreadFactory("redisson-delay-queue", false))
            .setWorkQueue(new LinkedBlockingQueue<>(10000))
            .build();

    @Resource
    private RedissonClient redissonMQClient;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        String targetSimpleName = GXRedissonDelayedQueueListener.class.getSimpleName();
        Class<?>[] allInterfacesForClass = ClassUtils.getAllInterfacesForClass(bean.getClass());
        for (Class<?> interfacesForClass : allInterfacesForClass) {
            if (interfacesForClass.getSimpleName().equals(targetSimpleName)) {
                startListenerThread(beanName, (GXRedissonDelayedQueueListener) bean);
                log.info("启动Redisson的延迟监听队列监听器完成 : {}", bean.getClass().getSimpleName());
            }
        }
        return bean;
    }

    /**
     * 启动线程Redisson的延迟队列线程
     * 该线程只会将获取出来的数据直接放入线程池中
     *
     * @param listenerThreadName           监听器的线程名字
     * @param redissonDelayedQueueListener 任务回调监听
     */
    private void startListenerThread(String listenerThreadName, GXRedissonDelayedQueueListener redissonDelayedQueueListener) {
        String queueName = redissonDelayedQueueListener.getDelayQueueName();
        RBlockingQueue<String> destinationQueue = redissonMQClient.getBlockingQueue(queueName);
        // 由于此线程需要常驻，可以新建线程，不用交给线程池管理
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    final String json = destinationQueue.take();
                    LOG.info("Redisson监听延迟队列线程 {} , 获取到值 : {}", queueName, json);
                    DELAY_QUEUE_EXECUTOR.submit(() -> redissonDelayedQueueListener.invoke(json));
                } catch (InterruptedException e) {
                    log.error("获取Redisson的延迟队列数据发生中断 : {} , {}", e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.setName(listenerThreadName);
        thread.start();
    }
}