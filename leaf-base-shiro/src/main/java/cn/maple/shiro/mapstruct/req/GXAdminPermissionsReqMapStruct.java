package cn.maple.shiro.mapstruct.req;

import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.shiro.dto.req.GXAdminPermissionsReqDto;
import cn.maple.shiro.entities.GXAdminPermissionsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXAdminPermissionsReqMapStruct extends GXBaseMapStruct<GXAdminPermissionsReqDto , GXAdminPermissionsEntity> {
}
