package cn.maple.shiro.mapstruct.req;

import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.shiro.dto.req.GXPermissionsReqDto;
import cn.maple.shiro.entities.GXPermissionsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXPermissionsReqMapStruct extends GXBaseMapStruct<GXPermissionsReqDto, GXPermissionsEntity> {
}
