package cn.maple.redisson.module.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.redisson.module.dto.req.GXRediSearchSchemaReqDto;

public class GXRediSearchEvent extends GXBaseEvent<GXRediSearchSchemaReqDto> {
    protected GXRediSearchEvent(GXRediSearchSchemaReqDto source, String eventName, Dict param, String scene) {
        super(source, eventName, param, scene);
    }

    public GXRediSearchEvent(GXRediSearchSchemaReqDto source) {
        super(source, "", Dict.create(), "");
    }
}
