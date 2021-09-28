package com.geoxus.feature.events;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.event.GXBaseEvent;

public class GXMediaLibraryEvent<T> extends GXBaseEvent<T> {
    public GXMediaLibraryEvent(T source, Dict param) {
        this(source, "", param, "");
    }

    public GXMediaLibraryEvent(T source, String eventName, Dict param) {
        this(source, eventName, param, "");
    }

    public GXMediaLibraryEvent(T source, String eventName, Dict param, Object scene) {
        super(source, eventName, param, scene);
    }
}
