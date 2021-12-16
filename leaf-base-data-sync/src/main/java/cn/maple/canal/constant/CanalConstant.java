package cn.maple.canal.constant;

public class CanalConstant {
    /**
     * RABBITMQ 消费CANAL的线程数量
     */
    public static final String RABBITMQ_CANAL_CONCURRENCY_COUNT = "10";

    /**
     * 监听的队列名字
     */
    public static final String RABBITMQ_CANAL_QUEUE_NAME = "example";

    /**
     * 交换机的名字
     * 对应 conf/canal.properties文件里面配置的 rabbitmq.exchange = exchange.fanout.canal
     */
    public static final String RABBITMQ_CANAL_EXCHANGE_NAME = "exchange.fanout.canal";

    /**
     * 交换机的名字
     * 对应 conf/example/instance.properties文件里面配置的  canal.mq.topic = canal.example.exchange.routingkey
     */
    public static final String RABBITMQ_CANAL_ROUTING_KEY = "canal.example.exchange.routingkey";

    private CanalConstant() {
    }
}
