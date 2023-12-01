package cn.maple.core.framework.api.dto.inner;

import cn.maple.core.framework.dto.req.GXBaseReqDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GXBaseServeApiConditionDto extends GXBaseReqDto {
    /**
     * 需要查询的字段名字
     */
    private String columnName;

    /**
     * 查询操作
     * GXBuilderConstant.IS_NULL
     */
    private String operator;

    /**
     * 查询的值
     */
    private Object value;
}
