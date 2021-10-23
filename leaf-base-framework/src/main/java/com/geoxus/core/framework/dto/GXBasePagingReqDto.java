package com.geoxus.core.framework.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBasePagingReqDto extends GXBaseDto {
    private static final long serialVersionUID = 8088371218041701430L;

    /**
     * 当前页
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 20;
}
