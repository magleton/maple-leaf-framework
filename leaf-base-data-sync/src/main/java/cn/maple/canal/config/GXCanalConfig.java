package cn.maple.canal.config;

import cn.maple.canal.properties.GXCanalProperties;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class GXCanalConfig {
    @Resource
    private GXCanalProperties canalProperties;

    @Bean
    public Binding binding(Exchange fanoutExchange, Queue canalQueue) {
        return BindingBuilder.bind(canalQueue).to(fanoutExchange).with(canalProperties.getRoutingKey()).noargs();
    }

    @Bean
    public Exchange fanoutExchange() {
        return new FanoutExchange(canalProperties.getExchangeName(), true, true);
    }

    @Bean
    public Queue canalQueue() {
        return new Queue(canalProperties.getCanalQueueName());
    }
}
