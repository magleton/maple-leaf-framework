package cn.maple.canal.properties;

import lombok.Data;

@Data
public class GXCanalProperties {
    /**
     * RABBITMQ 消费CANAL的线程数量
     */
    protected String concurrencyCount = "10";

    /**
     * canal的queue的名字
     * <p>
     * 监听的队列名字
     */
    protected String canalQueueName = "canalQueue";

    /**
     * 交换机的名字
     * 对应 conf/canal.properties文件里面配置的 rabbitmq.exchange = exchange.fanout.canal
     */
    protected String exchangeName = "exchange.fanout.canal";

    /**
     * 交换机的名字
     * 对应 conf/example/instance.properties文件里面配置的  canal.mq.topic = canal.example.exchange.routingkey
     */
    protected String routingKey = "canal.example.exchange.routingkey";
}
