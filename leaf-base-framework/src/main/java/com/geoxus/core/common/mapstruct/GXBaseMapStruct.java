package com.geoxus.core.common.mapstruct;

import com.geoxus.core.common.dto.GXBaseDto;
import com.geoxus.core.common.entity.GXBaseEntity;

import java.util.List;

/**
 * @author zj chen <britton@126.com>
 * @version 1.0
 * @since 2020-09-26
 */
public interface GXBaseMapStruct<D extends GXBaseDto, E extends GXBaseEntity> {
    /**
     * DTO转换为Entity
     *
     * @param dto DTO对象
     * @return E
     */
    E dtoToEntity(D dto);

    /**
     * Entity转换为DTO
     *
     * @param entity Entity对象
     * @return DTO
     */
    D entityToDto(E entity);

    /**
     * DTO列表转换为Entity列表
     *
     * @param dtoList DTO列表
     * @return List
     */
    List<E> dtoListToEntities(List<D> dtoList);

    /**
     * Entity列表转换为DTO列表
     *
     * @param entityList Entity列表
     * @return List
     */
    List<D> entityToDtoList(List<E> entityList);
}
