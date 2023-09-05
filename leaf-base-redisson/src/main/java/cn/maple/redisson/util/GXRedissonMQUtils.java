package cn.maple.redisson.util;

import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.redisson.adapter.GXRedissonDebeziumReliableTopic;
import org.redisson.Redisson;
import org.redisson.api.RFuture;
import org.redisson.api.RReliableTopic;
import org.redisson.api.RedissonClient;
import org.redisson.command.CommandAsyncExecutor;

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

    /**
     * 获取处理debezium server的topic
     *
     * @param redissonMQClient Redisson客户端对象
     * @param name             topic的名字
     * @param subscriberId     订阅ID
     * @return GXRedissonDebeziumReliableTopic
     */
    public static GXRedissonDebeziumReliableTopic getDebeziumReliableTopic(RedissonClient redissonMQClient, String name, String subscriberId) {
        CommandAsyncExecutor commandExecutor = ((Redisson) redissonMQClient).getCommandExecutor();
        return new GXRedissonDebeziumReliableTopic(commandExecutor, name, subscriberId);
    }

    /**
     * 获取处理debezium server的topic
     *
     * @param redissonMQClient Redisson客户端对象
     * @param name             topic的名字
     * @return GXRedissonDebeziumReliableTopic
     */
    public static GXRedissonDebeziumReliableTopic getDebeziumReliableTopic(RedissonClient redissonMQClient, String name) {
        return getDebeziumReliableTopic(redissonMQClient, name, null);
    }
}
