package com.geoxus.shiro.mapstruct;

import com.geoxus.shiro.dto.req.GXPermissionsReqDto;
import com.geoxus.shiro.entities.GXPermissionsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXPermissionsMapStruct {
    GXPermissionsReqDto entityToDto(GXPermissionsEntity entity);

    GXPermissionsEntity dtoToEntity(GXPermissionsReqDto permissionsReqDto);
}
