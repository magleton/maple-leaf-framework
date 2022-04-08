package cn.maple.core.framework.dto.protocol.req;

import cn.maple.core.framework.constant.GXCommonConstant;
import com.google.common.collect.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GXQueryParamReqProtocol extends GXBaseReqProtocol {
    private static final long serialVersionUID = -7685836286570517029L;

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
     */
    private transient Table<String, String, Object> condition;
}
