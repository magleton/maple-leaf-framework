package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.framework.event.GXBaseEvent;

public class GXModelUpdatingEvent<M extends GXMyBatisModel> extends GXBaseEvent<M> {
    public GXModelUpdatingEvent(M source, String eventName, Dict param) {
        super(source, eventName, param);
    }
}
