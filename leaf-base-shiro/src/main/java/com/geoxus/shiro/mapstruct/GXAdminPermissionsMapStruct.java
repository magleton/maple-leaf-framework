package com.geoxus.shiro.mapstruct;

import com.geoxus.shiro.dto.req.GXAdminPermissionsReqDto;
import com.geoxus.shiro.entities.GXAdminPermissionsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXAdminPermissionsMapStruct {
    GXAdminPermissionsReqDto entityToDto(GXAdminPermissionsEntity entity);

    GXAdminPermissionsEntity dtoToEntity(GXAdminPermissionsReqDto permissionsReqDto);
}
