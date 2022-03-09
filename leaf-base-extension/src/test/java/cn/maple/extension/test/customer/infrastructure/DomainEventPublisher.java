package cn.maple.extension.test.customer.infrastructure;

import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.util.GXEventPublisherUtils;
import cn.maple.core.framework.util.GXLoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DomainEventPublisher {
    public void publish(GXBaseEvent<?> domainEvent) {
        GXLoggerUtils.logInfo(log, "发布事件");
        GXEventPublisherUtils.publishEvent(domainEvent);
    }
}
