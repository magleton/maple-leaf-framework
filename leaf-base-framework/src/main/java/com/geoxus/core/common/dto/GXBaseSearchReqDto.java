package com.geoxus.core.common.dto;

import cn.hutool.core.lang.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseSearchReqDto extends GXBaseDto {
    /**
     * 分页信息
     * eg:
     * "pagingInfo":{
     * "page":1,
     * "pageSize":20
     * }
     */
    protected Dict pagingInfo;

    /**
     * 搜索条件
     * eg:
     * "searchCondition":{
     * "username":"jack",
     * "openId":"XXXXX"
     * }
     */
    protected Dict searchCondition;

    /**
     * 需要移除的字段
     * ext::username,ext::password
     * eg:
     * "removeField":["password" , "ext::username" , "info::password"]
     */
    protected Set<String> removeField;
}
