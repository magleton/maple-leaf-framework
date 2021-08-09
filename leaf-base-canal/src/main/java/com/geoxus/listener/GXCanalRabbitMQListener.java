package com.geoxus.listener;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.geoxus.constant.CanalConstant;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.dto.GXCanalDataDto;
import com.geoxus.service.GXProcessCanalDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@Slf4j
public class GXCanalRabbitMQListener {
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = CanalConstant.RABBITMQ_CANAL_QUEUE_NAME, durable = "true"),
                    exchange = @Exchange(value = CanalConstant.RABBITMQ_CANAL_EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
                    key = CanalConstant.RABBITMQ_CANAL_ROUTING_KEY
            )
    }, concurrency = CanalConstant.RABBITMQ_CANAL_CONCURRENCY_COUNT)
    public void listener(String message) {
        String operator = "operator";
        final GXCanalDataDto canalDataDto = JSONUtil.toBean(message, GXCanalDataDto.class);
        final String serviceName = CharSequenceUtil.toCamelCase(CharSequenceUtil.format("{}_{}_Service", canalDataDto.getDatabase(), canalDataDto.getTable()));
        final Object bean = GXSpringContextUtils.getBean(serviceName);
        if (Objects.isNull(bean)) {
            log.info("{}不存在,请提供实现了{}接口的类型", serviceName, GXProcessCanalDataService.class.getSimpleName());
            return;
        }
        if (!(bean instanceof GXProcessCanalDataService)) {
            log.info("{}必须是{}的子类", serviceName, GXProcessCanalDataService.class.getSimpleName());
            return;
        }
        switch (canalDataDto.getType()) {
            case "UPDATE":
                ((GXProcessCanalDataService) bean).processUpdate(canalDataDto, Dict.create().set(operator, "update"));
                break;
            case "INSERT":
                ((GXProcessCanalDataService) bean).processInsert(canalDataDto, Dict.create().set(operator, "insert"));
                break;
            case "DELETE":
                ((GXProcessCanalDataService) bean).processDelete(canalDataDto, Dict.create().set(operator, "delete"));
                break;
            default:
                log.info("RabbitMQ监听的canal没有对应的操作");
                break;
        }
    }
}