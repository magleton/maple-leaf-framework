package com.geoxus.shiro.mapstruct;

import com.geoxus.shiro.dto.req.GXAdminRoleReqDto;
import com.geoxus.shiro.entities.GXAdminRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXAdminRoleMapStruct {
    GXAdminRoleReqDto entityToDto(GXAdminRoleEntity entity);

    GXAdminRoleEntity dtoToEntity(GXAdminRoleReqDto permissionsReqDto);
}
