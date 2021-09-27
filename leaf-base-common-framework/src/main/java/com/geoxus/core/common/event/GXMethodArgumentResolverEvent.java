package com.geoxus.core.common.event;

import cn.hutool.core.lang.Dict;

public class GXMethodArgumentResolverEvent<T> extends GXBaseEvent<T> {
    private transient Dict dbFieldDict;

    public GXMethodArgumentResolverEvent(T requestFieldDict, String eventName, Dict param, Object scene) {
        super(requestFieldDict, eventName, param, scene);
    }

    public GXMethodArgumentResolverEvent(T requestFieldDict, Dict dbFieldDict, String eventName, Dict param, Object scene) {
        this(requestFieldDict, eventName, param, scene);
        this.dbFieldDict = dbFieldDict;
    }

    public Dict getDbFieldDict() {
        return dbFieldDict;
    }
}
