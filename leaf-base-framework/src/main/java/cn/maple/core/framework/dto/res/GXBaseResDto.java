package cn.maple.core.framework.dto.res;

import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseResDto extends GXBaseDto {
    /**
     * 额外处理逻辑
     * 当从数据库获取结果之后,可以自定一段逻辑
     * 如果有这段自定义逻辑,框架底层会自动调用这段处理逻辑
     */
    protected void extraHandle() {
        // Stub Code , Supplier Sub Class Customer
    }
}
