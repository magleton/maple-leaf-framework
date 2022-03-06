package cn.maple.redisson.module.event;

import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.redisson.module.dto.req.GXRedisJSONEventReqDto;

public class GXRedisJSONEvent extends GXBaseEvent<GXRedisJSONEventReqDto> {
    public GXRedisJSONEvent(GXRedisJSONEventReqDto source) {
        super(source);
    }
}
