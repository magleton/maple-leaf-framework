package cn.maple.core.framework.event;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import java.lang.reflect.Type;

@Getter
public class GXBaseEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {
    /**
     * 附加参数
     */
    private final Dict param;

    /**
     * 事件名字
     */
    private final String eventName;

    /**
     * 事件类型,用于区分同一个事件的不同使用场景
     */
    private final String eventType;

    public GXBaseEvent(T source) {
        this(source, "", Dict.create(), "");
    }

    public GXBaseEvent(T source, String eventType) {
        this(source, eventType, Dict.create(), "");
    }

    public GXBaseEvent(T source, String eventType, String eventName) {
        this(source, eventType, Dict.create(), eventName);
    }

    public GXBaseEvent(T source, String eventType, Dict param) {
        this(source, eventType, param, "");
    }

    public GXBaseEvent(T source, String eventType, Dict param, String eventName) {
        super(source);
        this.param = param;
        this.eventName = eventName;
        this.eventType = eventType;
    }

    public Object getEventName() {
        return eventName;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClass(getClass());
    }

    @Override
    public T getSource() {
        return Convert.convert((Type) source.getClass(), super.getSource());
    }
}
