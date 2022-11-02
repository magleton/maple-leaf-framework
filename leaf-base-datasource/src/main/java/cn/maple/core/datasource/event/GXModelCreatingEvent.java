package cn.maple.core.datasource.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.model.GXBaseModel;

/**
 * 创建数据之前的事件
 *
 * @param <M>
 */
public class GXModelCreatingEvent<M extends GXBaseModel> extends GXBaseEvent<M> {
    public GXModelCreatingEvent(M source, String eventName, Dict param) {
        super(source, eventName, param);
    }
}
