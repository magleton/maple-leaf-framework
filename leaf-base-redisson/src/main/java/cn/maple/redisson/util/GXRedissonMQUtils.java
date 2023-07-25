package cn.maple.redisson.util;

import cn.maple.core.framework.util.GXSpringContextUtils;
import org.redisson.api.RFuture;
import org.redisson.api.RReliableTopic;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;

public class GXRedissonMQUtils {
    private GXRedissonMQUtils() {

    }

    /**
     * 同步发布Redis的stream消息
     *
     * @param topicName 主题名字
     * @param message   发布的消息
     * @return 目前有多少条消息
     */
    public static long publish(String topicName, Object message) {
        RedissonClient redissonMQClient = GXSpringContextUtils.getBean("redissonMQClient", RedissonClient.class);
        assert redissonMQClient != null;
        RReliableTopic reliableTopic = redissonMQClient.getReliableTopic(topicName);
        return reliableTopic.publish(message);
    }

    /**
     * 异步发布Redis的stream消息
     *
     * @param topicName 主题名字
     * @param message   发布的消息
     * @return 目前有多少条消息
     */
    public static long publishAsync(String topicName, Object message) throws ExecutionException, InterruptedException {
        RedissonClient redissonMQClient = GXSpringContextUtils.getBean("redissonMQClient", RedissonClient.class);
        assert redissonMQClient != null;
        RReliableTopic reliableTopic = redissonMQClient.getReliableTopic(topicName);
        RFuture<Long> longRFuture = reliableTopic.publishAsync(message);
        return longRFuture.get();
    }
}
