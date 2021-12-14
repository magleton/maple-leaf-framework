package cn.maple.core.framework.ddd.annotation;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Entity, Entity Object is prototype and is not thread-safe
 * 标识对象是领域模型的实体对象
 *
 * @author britton
 * @since 2021-11-08
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public @interface GXDomainEntity {
}
