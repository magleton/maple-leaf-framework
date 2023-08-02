package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.model.GXBaseModel;

public class GXMyBatisModelUpdatingEntityEvent<M extends GXBaseModel> extends GXBaseEvent<M> {
    public GXMyBatisModelUpdatingEntityEvent(M source) {
        super(source);
    }

    public GXMyBatisModelUpdatingEntityEvent(M source, String eventName) {
        super(source, eventName);
    }

    public GXMyBatisModelUpdatingEntityEvent(M source, String eventName, Dict param) {
        super(source, eventName, param);
    }

    public GXMyBatisModelUpdatingEntityEvent(M source, String eventName, Dict param, String scene) {
        super(source, eventName, param, scene);
    }
}
