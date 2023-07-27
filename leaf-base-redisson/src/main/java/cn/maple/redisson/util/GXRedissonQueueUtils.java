package cn.maple.redisson.util;

import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.util.GXSpringContextUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class GXRedissonQueueUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(GXRedissonQueueUtils.class);

    private GXRedissonQueueUtils() {
    }

    /**
     * 发送同步延迟消息
     *
     * @param queueName 队列名字
     * @param message   发送的消息
     * @param delayTime 延迟时间
     * @param timeUnit  延迟时间单位
     */
    public static void sendDelayMessage(String queueName, Object message, int delayTime, TimeUnit timeUnit) {
        LOGGER.info("发送Redisson的延迟队列消息 : queueName = {} , message = {}", queueName, message);
        RDelayedQueue<String> delayedQueue = getDelayedQueue(queueName);
        delayedQueue.offer(JSONUtil.toJsonStr(message), delayTime, timeUnit);
    }

    /**
     * 发送同步延迟消息
     *
     * @param queueName 队列名字
     * @param message   发送的消息
     * @param delayTime 延迟时间
     * @param timeUnit  延迟时间单位
     */
    public static void sendAsyncDelayMessage(String queueName, Object message, int delayTime, TimeUnit timeUnit) {
        LOGGER.info("发送Redisson的延迟队列消息 : queueName = {} , message = {}", queueName, message);
        RDelayedQueue<String> delayedQueue = getDelayedQueue(queueName);
        delayedQueue.offer(JSONUtil.toJsonStr(message), delayTime, timeUnit);
    }

    /**
     * 获取延迟队列对象
     *
     * @param queueName 队列名字
     */
    private static RDelayedQueue<String> getDelayedQueue(String queueName) {
        RedissonClient redissonMQClient = GXSpringContextUtils.getBean("redissonMQClient", RedissonClient.class);
        assert redissonMQClient != null;
        RBlockingQueue<String> destinationQueue = redissonMQClient.getBlockingQueue(queueName);
        return redissonMQClient.getDelayedQueue(destinationQueue);
    }
}
