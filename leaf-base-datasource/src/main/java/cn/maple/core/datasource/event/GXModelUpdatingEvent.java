package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.model.GXBaseModel;

public class GXModelUpdatingEvent<M extends GXBaseModel> extends GXBaseEvent<M> {
    public GXModelUpdatingEvent(M source, String eventName, Dict param) {
        super(source, eventName, param);
    }
}
