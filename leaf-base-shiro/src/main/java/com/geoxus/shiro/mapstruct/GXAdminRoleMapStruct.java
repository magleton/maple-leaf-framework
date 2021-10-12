package com.geoxus.shiro.mapstruct;

import com.geoxus.shiro.dto.req.GXAdminRolesReqDto;
import com.geoxus.shiro.entities.GXAdminRolesEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXAdminRoleMapStruct {
    GXAdminRolesReqDto entityToDto(GXAdminRolesEntity entity);

    GXAdminRolesEntity dtoToEntity(GXAdminRolesReqDto permissionsReqDto);
}
