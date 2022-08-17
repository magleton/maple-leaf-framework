package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.framework.event.GXBaseEvent;

/**
 * 数据已经更新数据库事件
 */
public class GXModelUpdatedEvent<M extends GXMyBatisModel> extends GXBaseEvent<M> {
    public GXModelUpdatedEvent(M source, String eventName, Dict param) {
        super(source, eventName, param);
    }
}
