package cn.maple.core.framework.dto.req;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.constant.GXCommonConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseQueryParamReqDto extends GXBaseReqDto {
    private static final long serialVersionUID = 8088371218041701430L;

    /**
     * 当前页
     */
    private Integer page = GXCommonConstant.DEFAULT_CURRENT_PAGE;

    /**
     * 每页大小
     */
    private Integer pageSize = GXCommonConstant.DEFAULT_PAGE_SIZE;

    /**
     * 查询条件
     * eg:
     * Dict condition = Dict.create();
     * condition.set("username","枫叶思源");
     */
    private Dict condition;
}
