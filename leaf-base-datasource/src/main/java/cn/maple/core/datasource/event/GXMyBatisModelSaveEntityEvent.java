package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;

/**
 * 数据已经插入数据库事件
 */
public class GXMyBatisModelSaveEntityEvent<M extends Dict> extends GXBaseEvent<M> {
    public GXMyBatisModelSaveEntityEvent(M source) {
        super(source);
    }

    public GXMyBatisModelSaveEntityEvent(M source, String eventType) {
        super(source, eventType);
    }

    public GXMyBatisModelSaveEntityEvent(M source, String eventType, Dict param) {
        super(source, eventType, param);
    }

    public GXMyBatisModelSaveEntityEvent(M source, String eventType, Dict param, String eventName) {
        super(source, eventType, param, eventName);
    }
}
