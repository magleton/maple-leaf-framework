package com.geoxus.shiro.mapstruct.req;

import com.geoxus.core.framework.mapstruct.GXBaseMapStruct;
import com.geoxus.shiro.dto.req.GXRoleReqDto;
import com.geoxus.shiro.entities.GXRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXRoleReqMapStruct extends GXBaseMapStruct<GXRoleReqDto, GXRoleEntity> {
}
