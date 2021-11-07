package cn.maple.core.framework.ddd.publisher;

import cn.maple.core.framework.exception.GXBusinessException;

/**
 * DomainEventPublisher, this is for demo purpose, the Event is sent to EventBus
 * <p>
 * Normally DomainEvent should be sent to Messaging Middleware
 *
 * @author britton
 * @since 2021-11-07
 */
public interface GXDomainEventPublisher {
    default void publish(Object domainEvent) {
        throw new GXBusinessException("实现自定义领域事件派发器!");
    }
}
