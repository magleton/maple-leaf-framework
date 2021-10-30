package cn.maple.shiro.mapstruct.req;

import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.shiro.dto.protocol.req.GXAdminReqProtocol;
import cn.maple.shiro.dto.req.GXAdminReqDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXAdminReqMapStruct extends GXBaseMapStruct<GXAdminReqProtocol, GXAdminReqDto> {
}
