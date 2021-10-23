package com.geoxus.order.builder.dto;

import com.geoxus.core.framework.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddressDto extends GXBaseDto {
    private Long userId;

    private String username;
}
