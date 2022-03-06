package cn.maple.redisson.module.event;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.redisson.module.dto.req.GXRediSearchEventReqDto;

public class GXRediSearchEvent extends GXBaseEvent<GXRediSearchEventReqDto> {
    protected GXRediSearchEvent(GXRediSearchEventReqDto source, String eventName, Dict param, String scene) {
        super(source, eventName, param, scene);
    }

    public GXRediSearchEvent(GXRediSearchEventReqDto source) {
        super(source, "", Dict.create(), "");
    }
}
