package com.geoxus.core.common.mapstruct;

import com.geoxus.core.common.dto.GXBaseDto;
import com.geoxus.core.common.entity.GXBaseEntity;

/**
 * @author zj chen <britton@126.com>
 * @version 1.0
 * @since 2020-09-26
 */
public interface GXBaseMapStruct<D extends GXBaseDto, E extends GXBaseEntity> {
    E dtoToEntity(D dto);

    D entityToDto(E entity);
}
