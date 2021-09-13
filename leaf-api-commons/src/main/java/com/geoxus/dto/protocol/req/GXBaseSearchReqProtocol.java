package com.geoxus.dto.protocol.req;

import cn.hutool.core.lang.Dict;
import com.geoxus.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseSearchReqProtocol extends GXBaseDto {
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
}
