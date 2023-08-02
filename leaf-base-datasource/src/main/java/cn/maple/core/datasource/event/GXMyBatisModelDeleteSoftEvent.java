package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.lang.GXDict;

public class GXMyBatisModelDeleteSoftEvent<M extends GXDict> extends GXBaseEvent<M> {
    public GXMyBatisModelDeleteSoftEvent(M source) {
        super(source);
    }

    public GXMyBatisModelDeleteSoftEvent(M source, String eventName) {
        super(source, eventName);
    }

    public GXMyBatisModelDeleteSoftEvent(M source, String eventName, Dict param) {
        super(source, eventName, param);
    }

    public GXMyBatisModelDeleteSoftEvent(M source, String eventName, Dict param, String scene) {
        super(source, eventName, param, scene);
    }
}
