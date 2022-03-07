package cn.maple.redisson.module.dto.req;

import cn.maple.core.framework.dto.req.GXBaseReqDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXRediSearchEventReqDto extends GXBaseReqDto {
    /**
     * 索引名字
     */
    private String indexName;

    /**
     * 索引所依赖的底层数据类型
     * 1、HASH
     * 2、JSON
     */
    private String dataType;

    /**
     * 所依赖底层数据的前缀
     */
    private String prefix;

    /**
     * filter
     */
    private String filter;
}
