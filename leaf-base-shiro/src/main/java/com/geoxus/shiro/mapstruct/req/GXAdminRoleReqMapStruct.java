package com.geoxus.shiro.mapstruct.req;

import com.geoxus.core.framework.mapstruct.GXBaseMapStruct;
import com.geoxus.shiro.dto.req.GXAdminRolesReqDto;
import com.geoxus.shiro.entities.GXAdminRolesEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXAdminRoleReqMapStruct extends GXBaseMapStruct<GXAdminRolesReqDto, GXAdminRolesEntity> {
}
