package cn.maple.rabbitmq.dto.inner;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class GXRabbitMQMessageReqDto extends GXBaseReqDto {
    /**
     * 交换机
     */
    private String exchange;

    /**
     * 路由key
     */
    private String routingKey;

    /**
     * 消息数据
     */
    private Dict data;

    /**
     * 消费tag
     */
    private String tag;

    /**
     * 发送事务消息需要
     */
    private transient CorrelationData correlationData = new CorrelationData();

    /**
     * 消息属性
     */
    private MessageProperties messageProperties = new MessageProperties();
}