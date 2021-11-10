package cn.maple.core.framework.dto.inner.req;

import cn.hutool.core.lang.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseQueryParamReqDto extends GXBaseReqDto {
    private static final long serialVersionUID = 8088371218041701430L;

    /**
     * 当前页
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 20;

    /**
     * 查询条件
     * eg:
     * Dict condition = Dict.create();
     * condition.set("username" ,"枫叶思源");
     */
    private Dict queryCondition;
}
