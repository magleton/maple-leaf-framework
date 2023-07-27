package cn.maple.redisson.processor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.ObjectUtil;
import cn.maple.redisson.annotation.GXConvertRedissonDelayQueueToTopic;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RReliableTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

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
    private static final ExecutorService DELAY_QUEUE_EXECUTOR = ExecutorBuilder.create().setCorePoolSize(2).setMaxPoolSize(10).setThreadFactory(new NamedThreadFactory("redisson-delay-queue", false)).setWorkQueue(new LinkedBlockingQueue<>(10000)).build();

    @Resource
    private RedissonClient redissonMQClient;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object singletonTarget = AopProxyUtils.getSingletonTarget(bean);
        if (ObjectUtil.isNull(singletonTarget)) {
            return bean;
        }
        GXConvertRedissonDelayQueueToTopic convertRedissonQueueToTopic = singletonTarget.getClass().getAnnotation(GXConvertRedissonDelayQueueToTopic.class);
        if (ObjectUtil.isNotNull(convertRedissonQueueToTopic)) {
            String delayQueueName = convertRedissonQueueToTopic.delayQueueName();
            String topicName = convertRedissonQueueToTopic.topicName();
            RBlockingQueue<String> destinationQueue = redissonMQClient.getBlockingQueue(delayQueueName);
            RReliableTopic reliableTopic = redissonMQClient.getReliableTopic(topicName);
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        String json = destinationQueue.take();
                        reliableTopic.publish(json);
                        LOG.info("Redisson监听到的延迟队列【{}】中的数据【{}】被投递到【{}】Stream流中", delayQueueName, json, topicName);
                    } catch (InterruptedException e) {
                        log.error("获取Redisson的延迟队列【{}】数据发生中断 : {} , {}", delayQueueName, e.getMessage(), e);
                        Thread.currentThread().interrupt();
                    }
                }
            });
            thread.setName(CharSequenceUtil.format("{}-thread", delayQueueName));
            thread.start();
        }
        return bean;
    }
}