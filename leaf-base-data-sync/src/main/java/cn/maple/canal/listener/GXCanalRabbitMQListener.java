package cn.maple.canal.listener;

import cn.maple.canal.constant.CanalConstant;
import cn.maple.canal.service.GXCanalMessageParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@Lazy
public class GXCanalRabbitMQListener {
    @Resource
    private GXCanalMessageParseService canalMessageParseService;

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "#{T(cn.maple.core.framework.util.GXSpELToolUtils).callBeanMethodSpELExpression(Class.forName('cn.maple.rabbitmq.properties.GXRabbitMQProperties'), 'getCanalQueue', Class.forName('java.lang.String'), new Class[0])}", durable = "true"), exchange = @Exchange(value = CanalConstant.RABBITMQ_CANAL_EXCHANGE_NAME, type = ExchangeTypes.FANOUT), key = CanalConstant.RABBITMQ_CANAL_ROUTING_KEY)}, concurrency = CanalConstant.RABBITMQ_CANAL_CONCURRENCY_COUNT)
    public void listener(String message) {
        canalMessageParseService.parseMessage(message);
    }
}