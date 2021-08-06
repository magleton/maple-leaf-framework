package com.geoxus.shiro.mapstruct;

import com.geoxus.shiro.dto.req.GXRolePermissionsReqDto;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXRolePermissionsMapStruct {
    GXRolePermissionsReqDto entityToDto(GXRolePermissionsEntity entity);

    GXRolePermissionsEntity dtoToEntity(GXRolePermissionsReqDto permissionsReqDto);
}
