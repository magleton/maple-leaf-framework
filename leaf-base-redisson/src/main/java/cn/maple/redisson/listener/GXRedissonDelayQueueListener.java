package cn.maple.redisson.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.framework.util.GXSpringContextUtils;
import org.redisson.api.RReliableTopic;
import org.redisson.api.RedissonClient;

/**
 * 延迟队列数据到期时的监听器
 */
public interface GXRedissonDelayQueueListener {
    default void execute(String topicName, String message) {
        RedissonClient redissonMQClient = GXSpringContextUtils.getBean("redissonMQClient", RedissonClient.class);
        if (ObjectUtil.isNotNull(redissonMQClient)) {
            RReliableTopic reliableTopic = redissonMQClient.getReliableTopic(topicName);
            reliableTopic.publish(message);
        }
    }
}
