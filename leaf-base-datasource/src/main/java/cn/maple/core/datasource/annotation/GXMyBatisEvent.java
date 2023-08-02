package cn.maple.core.datasource.annotation;

import cn.maple.core.framework.event.GXBaseEvent;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GXMyBatisEvent {
    Class<? extends GXBaseEvent<?>> eventClass();
}
