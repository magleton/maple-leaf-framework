package cn.maple.redisson.module.dto.req;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Builder
public class GXRedisJSONReqDto extends GXBaseReqDto {
    /**
     * Redis的key
     */
    private String key;

    /**
     * JSON的路径
     */
    private String path;

    /**
     * JSON数据
     */
    private Dict data;
}
