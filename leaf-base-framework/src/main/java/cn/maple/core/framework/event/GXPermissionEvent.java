package cn.maple.core.framework.event;

import cn.hutool.core.lang.Dict;

public class GXPermissionEvent<T> extends GXBaseEvent<T> {
    public GXPermissionEvent(T source, Dict param) {
        this(source, "", param, "");
    }

    public GXPermissionEvent(T source, String eventName, Dict param) {
        this(source, eventName, param, "");
    }

    public GXPermissionEvent(T source, String eventName, Dict param, Object scene) {
        super(source, eventName, param, scene);
    }
}
