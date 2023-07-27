package cn.maple.redisson.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXSpringContextUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
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
        String msg = convertMessageToString(message);
        LOGGER.info("发送Redisson的延迟队列消息 : queueName = {} , message = {}", queueName, msg);
        RDelayedQueue<String> delayedQueue = getDelayedQueue(queueName);
        delayedQueue.offer(JSONUtil.toJsonStr(msg), delayTime, timeUnit);
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
        String msg = convertMessageToString(message);
        LOGGER.info("发送Redisson的延迟队列消息 : queueName = {} , message = {}", queueName, msg);
        RDelayedQueue<String> delayedQueue = getDelayedQueue(queueName);
        delayedQueue.offer(JSONUtil.toJsonStr(msg), delayTime, timeUnit);
    }

    /**
     * 获取延迟队列对象
     *
     * @param queueName 队列名字
     */
    public static RDelayedQueue<String> getDelayedQueue(String queueName) {
        RedissonClient redissonMQClient = GXSpringContextUtils.getBean("redissonMQClient", RedissonClient.class);
        if (Objects.isNull(redissonMQClient)) {
            throw new GXBusinessException("请配置redissonMQClient对象");
        }
        RBlockingQueue<String> destinationQueue = redissonMQClient.getBlockingQueue(queueName);
        return redissonMQClient.getDelayedQueue(destinationQueue);
    }

    /**
     * 将传递进来的的对象转换成字符串
     * 如果是一个对象  则将其转换成json
     *
     * @param message 待转换的对象
     */
    private static String convertMessageToString(Object message) {
        if (!(ClassUtil.isBasicType(message.getClass()) || String.class.isAssignableFrom(message.getClass()))) {
            return JSONUtil.toJsonStr(message);
        }
        return Convert.convert(String.class, message);
    }
}
