package cn.maple.shiro.mapstruct.req;

import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.shiro.dto.req.GXRoleReqDto;
import cn.maple.shiro.entities.GXRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXRoleReqMapStruct extends GXBaseMapStruct<GXRoleReqDto, GXRoleEntity> {
}
