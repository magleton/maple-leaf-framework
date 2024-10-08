package cn.maple.rabbitmq.listener.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.rabbitmq.listener.GXRabbitMQQueueListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@Lazy
@ConditionalOnExpression("'${enable-rabbitmq}'.equals('true')")
@ConditionalOnClass(name = {"org.springframework.amqp.rabbit.connection.ConnectionFactory"})
public class GXDefaultRabbitMQQueueListenerImpl implements GXRabbitMQQueueListener {
    @Override
    @RabbitHandler
    @RabbitListener(queues = "#{T(cn.maple.core.framework.util.GXSpELToolUtils).callBeanMethodSpELExpression(Class.forName('cn.maple.rabbitmq.properties.GXRabbitMQProperties'), 'getDefaultQueueName', Class.forName('java.lang.String'), new Class[0])}")
    public void process(Message data) {
        final String s = new String(data.getBody(), StandardCharsets.UTF_8);
        if (CharSequenceUtil.isNotBlank(s)) {
            if (JSONUtil.isTypeJSON(s)) {
                final Dict param = JSONUtil.toBean(s, Dict.class);
                log.debug("param  : " + param);
            }
            log.debug("byte[]  : " + s);
        }
    }
}
