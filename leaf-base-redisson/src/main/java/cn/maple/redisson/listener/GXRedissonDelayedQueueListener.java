package cn.maple.redisson.listener;

public interface GXRedissonDelayedQueueListener {
    /**
     * 执行任务
     *
     * @param json 参数必须是JSON对象
     */
    void invoke(String json);

    /**
     * 获取队列的名字
     */
    String getDelayQueueName();

    /**
     * 配置topic的名字
     */
    String getTopicName();
}
