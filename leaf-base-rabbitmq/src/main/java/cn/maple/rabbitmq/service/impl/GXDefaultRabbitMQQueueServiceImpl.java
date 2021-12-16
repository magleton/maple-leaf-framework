package cn.maple.rabbitmq.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.rabbitmq.service.GXRabbitMQQueueService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@NacosPropertySource(groupId = "${nacos.config.group:DEFAULT_GROUP}", dataId = "rabbit.yml", autoRefreshed = true, type = ConfigType.YAML)
@ConditionalOnExpression("'${enable-rabbitmq}'.equals('true')")
@ConditionalOnClass(name = {"org.springframework.amqp.rabbit.connection.ConnectionFactory"})
public class GXDefaultRabbitMQQueueServiceImpl implements GXRabbitMQQueueService {
    @Override
    @RabbitHandler
    @RabbitListener(queues = {"${rabbit.default-queue-name}"})
    public void process(Message data) {
        final String s = new String(data.getBody(), StandardCharsets.UTF_8);
        if (CharSequenceUtil.isNotBlank(s)) {
            if (JSONUtil.isJson(s)) {
                final Dict param = JSONUtil.toBean(s, Dict.class);
                log.debug("param  : " + param);
            }
            log.debug("byte[]  : " + s);
        }
    }
}
