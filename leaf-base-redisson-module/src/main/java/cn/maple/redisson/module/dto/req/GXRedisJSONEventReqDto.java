package cn.maple.redisson.module.dto.req;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXRedisJSONEventReqDto extends GXBaseReqDto {
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
