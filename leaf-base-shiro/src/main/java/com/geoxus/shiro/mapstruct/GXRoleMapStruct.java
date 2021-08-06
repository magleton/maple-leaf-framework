package com.geoxus.shiro.mapstruct;

import com.geoxus.shiro.dto.req.GXRoleReqDto;
import com.geoxus.shiro.entities.GXRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXRoleMapStruct {
    GXRoleReqDto entityToDto(GXRoleEntity entity);

    GXRoleEntity dtoToEntity(GXRoleReqDto permissionsReqDto);
}
