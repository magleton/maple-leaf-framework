package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.model.GXBaseModel;

/**
 * 数据已经插入数据库事件
 */
public class GXMyBatisModelCreateEntityEvent<M extends GXBaseModel> extends GXBaseEvent<M> {
    public GXMyBatisModelCreateEntityEvent(M source) {
        super(source);
    }

    public GXMyBatisModelCreateEntityEvent(M source, String eventName) {
        super(source, eventName);
    }

    public GXMyBatisModelCreateEntityEvent(M source, String eventName, Dict param) {
        super(source, eventName, param);
    }

    public GXMyBatisModelCreateEntityEvent(M source, String eventName, Dict param, String scene) {
        super(source, eventName, param, scene);
    }
}
