package cn.maple.core.framework.event;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import java.lang.reflect.Type;

public class GXBaseEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {
    /**
     * 附加参数
     */
    @Getter
    private final Dict param;

    /**
     * 场景值,用于区分同一个事件的不同使用场景
     */
    private final String scene;

    /**
     * 事件名字
     */
    @Getter
    private final String eventName;

    public GXBaseEvent(T source) {
        this(source, "", Dict.create(), "");
    }

    public GXBaseEvent(T source, String eventName) {
        this(source, eventName, Dict.create(), "");
    }

    public GXBaseEvent(T source, String eventName, String scene) {
        this(source, eventName, Dict.create(), scene);
    }

    public GXBaseEvent(T source, String eventName, Dict param) {
        this(source, eventName, param, "");
    }

    public GXBaseEvent(T source, String eventName, Dict param, String scene) {
        super(source);
        this.param = param;
        this.scene = scene;
        this.eventName = eventName;
    }

    public Object getScene() {
        return scene;
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
