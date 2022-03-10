package cn.maple.redisson.module.event;

import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.redisson.module.dto.req.GXRedisJSONReqDto;

public class GXRedisJSONEvent extends GXBaseEvent<GXRedisJSONReqDto> {
    public GXRedisJSONEvent(GXRedisJSONReqDto source) {
        super(source);
    }
}
