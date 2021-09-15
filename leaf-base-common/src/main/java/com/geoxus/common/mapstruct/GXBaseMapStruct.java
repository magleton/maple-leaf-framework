package com.geoxus.common.mapstruct;

import com.geoxus.common.dto.GXBaseDto;

import java.util.List;

/**
 * @author britton <britton@126.com>
 * @version 1.0
 * @since 2020-09-26
 */
public interface GXBaseMapStruct<D extends GXBaseDto, E> {
    /**
     * DTO转换为Entity
     *
     * @param dto WEB请求对象
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
     * @param dtoList WEB请求对象列表
     * @return List
     */
    List<E> dtoListToEntities(List<D> dtoList);

    /**
     * Entity列表转换为DTO列表
     *
     * @param entities Entity列表
     * @return List
     */
    List<D> entitiesToDtoList(List<E> entities);
}
