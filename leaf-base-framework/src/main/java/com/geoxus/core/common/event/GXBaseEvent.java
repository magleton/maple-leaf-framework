package com.geoxus.core.common.event;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import java.lang.reflect.Type;

public abstract class GXBaseEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {
    @GXFieldCommentAnnotation(zhDesc = "附加参数")
    protected transient Dict param;

    @GXFieldCommentAnnotation(zhDesc = "场景值,用于区分同一个事件的不同使用场景")
    protected transient Object scene;

    @GXFieldCommentAnnotation(zhDesc = "事件名字")
    protected transient String eventName;

    protected GXBaseEvent(T source, String eventName, Dict param, Object scene) {
        super(source);
        this.param = param;
        this.scene = scene;
        this.eventName = eventName;
    }

    public Dict getParam() {
        return param;
    }

    public Object getScene() {
        return scene;
    }

    public String getEventName() {
        return eventName;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getSource()));
    }

    @Override
    public T getSource() {
        return Convert.convert((Type) source.getClass(), super.getSource());
    }
}
