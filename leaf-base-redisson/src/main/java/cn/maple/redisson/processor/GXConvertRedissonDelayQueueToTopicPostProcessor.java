package cn.maple.redisson.processor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.maple.redisson.annotation.GXConvertRedissonDelayQueueToTopic;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RReliableTopic;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 处理标注了GXConvertRedissonDelayQueueToTopic注解的bean
 * 标注了该注解的bean可以将delayQueue中到期了的数据重新放入
 * Stream数据结构中 实现可靠的消息队列
 */
@Component
@Log4j2
@Lazy
public class GXConvertRedissonDelayQueueToTopicPostProcessor implements BeanPostProcessor {
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
                        // 此处可以将任务放入线程池中
                        reliableTopic.publish(json);
                        log.info("Redisson监听到的延迟队列【{}】中的数据【{}】被投递到【{}】Stream流中", delayQueueName, json, topicName);
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