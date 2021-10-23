package com.geoxus.core.framework.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class GXBaseDto extends GXBaseData {
    private static final long serialVersionUID = 1887705447072701970L;
}
