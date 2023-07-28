package cn.maple.redisson.processor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.redisson.annotation.GXConvertRedissonDelayQueueToTopic;
import cn.maple.redisson.listener.GXRedissonDelayQueueListener;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 标注了GXConvertRedissonDelayQueueToTopic注解的bean
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
            Object listenerBean = getListenerBean(bean, convertRedissonQueueToTopic);
            String delayQueueName = convertRedissonQueueToTopic.delayQueueName();
            String topicName = convertRedissonQueueToTopic.topicName();
            int timeout = convertRedissonQueueToTopic.timeout();
            RBlockingQueue<String> destinationQueue = redissonMQClient.getBlockingQueue(delayQueueName);
            String threadName = CharSequenceUtil.format("redisson-delay-queue-{}-thread", delayQueueName);
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        log.info("线程【{}】执行获取【{}】延迟队列中的数据", Thread.currentThread().getName(), delayQueueName);
                        String message = destinationQueue.pollFromAny(timeout, TimeUnit.SECONDS);
                        // 可以将到期的数据存入持久化介质中 保证持久化介质的可靠性 可以提高该方案的可靠性
                        if (CharSequenceUtil.isNotEmpty(message)) {
                            GXCommonUtils.reflectCallObjectMethod(listenerBean, "execute", topicName, message);
                        }
                        if (CharSequenceUtil.isEmpty(message)) {
                            message = "！！！No message was received！！！";
                        }
                        log.info("Redisson监听到的延迟队列【{}】中的数据【{}】被投递到【{}】Stream流中", delayQueueName, message, topicName);
                    } catch (InterruptedException e) {
                        log.error("获取Redisson的延迟队列【{}】数据发生中断 : {} , {}", delayQueueName, e.getMessage(), e);
                        Thread.currentThread().interrupt();
                    }
                }
            });
            thread.setName(threadName);
            thread.start();
        }
        return bean;
    }

    /**
     * 获取实现了GXRedissonDelayQueueListener接口并且标注了GXConvertRedissonDelayQueueToTopic注解的bean
     */
    private Object getListenerBean(Object bean, GXConvertRedissonDelayQueueToTopic convertRedissonQueueToTopic) {
        if (ObjectUtil.isNotNull(convertRedissonQueueToTopic)) {
            String targetSimpleName = GXRedissonDelayQueueListener.class.getSimpleName();
            Class<?>[] allInterfacesForClass = ClassUtils.getAllInterfacesForClass(bean.getClass());
            for (Class<?> interfacesForClass : allInterfacesForClass) {
                if (interfacesForClass.getSimpleName().equals(targetSimpleName)) {
                    return bean;
                }
            }
        }
        return null;
    }
}
