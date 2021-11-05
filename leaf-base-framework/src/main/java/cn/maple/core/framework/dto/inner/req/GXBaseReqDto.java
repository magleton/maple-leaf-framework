package cn.maple.core.framework.dto.inner.req;

import cn.maple.core.framework.dto.GXBaseDto;
import cn.maple.core.framework.util.GXValidatorUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseReqDto extends GXBaseDto {
    @Override
    public void verify() {
        GXValidatorUtil.validateEntity(this);
    }
}
