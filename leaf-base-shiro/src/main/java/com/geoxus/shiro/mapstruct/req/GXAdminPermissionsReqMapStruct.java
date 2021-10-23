package com.geoxus.shiro.mapstruct.req;

import com.geoxus.common.mapstruct.GXBaseMapStruct;
import com.geoxus.shiro.dto.req.GXAdminPermissionsReqDto;
import com.geoxus.shiro.entities.GXAdminPermissionsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXAdminPermissionsReqMapStruct extends GXBaseMapStruct<GXAdminPermissionsReqDto , GXAdminPermissionsEntity> {
}
