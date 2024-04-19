package cn.maple.core.framework.event.dto;

import cn.maple.core.framework.dto.GXBaseEventDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXExceptionNotifyEventDto extends GXBaseEventDto {
    private Throwable throwable;
}