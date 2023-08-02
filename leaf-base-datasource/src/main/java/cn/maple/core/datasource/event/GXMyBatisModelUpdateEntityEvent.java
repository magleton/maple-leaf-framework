package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.model.GXBaseModel;

/**
 * 数据已经更新数据库事件
 */
public class GXMyBatisModelUpdateEntityEvent<M extends GXBaseModel> extends GXBaseEvent<M> {
    public GXMyBatisModelUpdateEntityEvent(M source) {
        super(source);
    }

    public GXMyBatisModelUpdateEntityEvent(M source, String eventName) {
        super(source, eventName);
    }

    public GXMyBatisModelUpdateEntityEvent(M source, String eventName, Dict param) {
        super(source, eventName, param);
    }

    public GXMyBatisModelUpdateEntityEvent(M source, String eventName, Dict param, String scene) {
        super(source, eventName, param, scene);
    }
}
