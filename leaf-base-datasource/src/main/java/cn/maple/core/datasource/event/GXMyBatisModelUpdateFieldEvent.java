package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.lang.GXDict;
import cn.maple.core.framework.model.GXBaseModel;

public class GXMyBatisModelUpdateFieldEvent<M extends GXDict> extends GXBaseEvent<M> {
    public GXMyBatisModelUpdateFieldEvent(M source) {
        super(source);
    }

    public GXMyBatisModelUpdateFieldEvent(M source, String eventName) {
        super(source, eventName);
    }

    public GXMyBatisModelUpdateFieldEvent(M source, String eventName, Dict param) {
        super(source, eventName, param);
    }

    public GXMyBatisModelUpdateFieldEvent(M source, String eventName, Dict param, String scene) {
        super(source, eventName, param, scene);
    }
}
