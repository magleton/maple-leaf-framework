package cn.maple.shiro.mapstruct.req;

import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.shiro.dto.req.GXAdminRolesReqDto;
import cn.maple.shiro.entities.GXAdminRolesEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXAdminRoleReqMapStruct extends GXBaseMapStruct<GXAdminRolesReqDto, GXAdminRolesEntity> {
}
