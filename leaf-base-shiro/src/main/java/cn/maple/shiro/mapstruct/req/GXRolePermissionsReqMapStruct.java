package cn.maple.shiro.mapstruct.req;

import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.shiro.dto.req.GXRolePermissionsReqDto;
import cn.maple.shiro.entities.GXRolePermissionsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXRolePermissionsReqMapStruct extends GXBaseMapStruct<GXRolePermissionsReqDto, GXRolePermissionsEntity> {
}
