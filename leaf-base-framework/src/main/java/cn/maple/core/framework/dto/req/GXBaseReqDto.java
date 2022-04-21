package cn.maple.core.framework.dto.req;

import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseReqDto extends GXBaseDto {
    /**
     * @author britton
     * 在验证请求参数之前进行数据修复(自动填充一些信息)
     */
    protected void beforeRepair() {
    }

    /**
     * @author britton
     * 参数
     * 验证请求参数之后进行数据修复(自动填充一些信息)
     */
    protected void afterRepair() {
    }
}
