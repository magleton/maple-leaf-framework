package cn.maple.core.framework.dto.protocol.req;

import cn.hutool.core.lang.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GXQueryParamReqProtocol extends GXBaseReqProtocol {
    private static final long serialVersionUID = -7685836286570517029L;

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
     * {
     * "username":"枫叶思源"
     * }
     */
    private Dict queryCondition;
}
