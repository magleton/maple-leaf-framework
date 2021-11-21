package cn.maple.core.framework.event;

import cn.hutool.core.lang.Dict;

public class GXMenuPermissionEvent<T> extends GXBaseEvent<T> {
    public GXMenuPermissionEvent(T source, Dict param) {
        this(source, "", param, "");
    }

    public GXMenuPermissionEvent(T source, String eventName, Dict param) {
        this(source, eventName, param, "");
    }

    public GXMenuPermissionEvent(T source, String eventName, Dict param, Object scene) {
        super(source, eventName, param, scene);
    }
}
