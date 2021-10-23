package com.geoxus.shiro.mapstruct.req;

import com.geoxus.common.mapstruct.GXBaseMapStruct;
import com.geoxus.shiro.dto.req.GXRolePermissionsReqDto;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXRolePermissionsReqMapStruct extends GXBaseMapStruct<GXRolePermissionsReqDto, GXRolePermissionsEntity> {
}
