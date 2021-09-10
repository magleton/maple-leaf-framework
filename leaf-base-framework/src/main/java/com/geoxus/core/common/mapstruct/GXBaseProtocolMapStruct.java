package com.geoxus.core.common.mapstruct;

import com.geoxus.core.common.dto.protocol.GXBaseReqProtocol;
import com.geoxus.core.common.entity.GXBaseEntity;

import java.util.List;

/**
 * @author zj chen <britton@126.com>
 * @version 1.0
 * @since 2020-09-26
 */
public interface GXBaseProtocolMapStruct<D extends GXBaseReqProtocol, E extends GXBaseEntity> {
    /**
     * DTO转换为Entity
     *
     * @param protocol WEB请求对象
     * @return E
     */
    E protocolToEntity(D protocol);

    /**
     * Entity转换为DTO
     *
     * @param entity Entity对象
     * @return DTO
     */
    D entityToProtocol(E entity);

    /**
     * DTO列表转换为Entity列表
     *
     * @param protocolList WEB请求对象列表
     * @return List
     */
    List<E> protocolListToEntities(List<D> protocolList);

    /**
     * Entity列表转换为DTO列表
     *
     * @param entities Entity列表
     * @return List
     */
    List<D> entitiesToProtocolList(List<E> entities);
}
