package com.geoxus.shiro.mapstruct.req;

import com.geoxus.core.framework.mapstruct.GXBaseMapStruct;
import com.geoxus.shiro.dto.req.GXPermissionsReqDto;
import com.geoxus.shiro.entities.GXPermissionsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXPermissionsReqMapStruct extends GXBaseMapStruct<GXPermissionsReqDto, GXPermissionsEntity> {
}
