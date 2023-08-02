package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.model.GXBaseModel;

/**
 * 创建数据之前的事件
 *
 * @param <M>
 */
public class GXMyBatisModelCreatingEntityEvent<M extends GXBaseModel> extends GXBaseEvent<M> {
    public GXMyBatisModelCreatingEntityEvent(M source) {
        super(source);
    }

    public GXMyBatisModelCreatingEntityEvent(M source, String eventName) {
        super(source, eventName);
    }

    public GXMyBatisModelCreatingEntityEvent(M source, String eventName, Dict param) {
        super(source, eventName, param);
    }

    public GXMyBatisModelCreatingEntityEvent(M source, String eventName, Dict param, String scene) {
        super(source, eventName, param, scene);
    }
}
