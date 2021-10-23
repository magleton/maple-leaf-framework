package com.geoxus.shiro.mapstruct.req;

import com.geoxus.common.mapstruct.GXBaseMapStruct;
import com.geoxus.shiro.dto.protocol.req.GXAdminReqProtocol;
import com.geoxus.shiro.dto.req.GXAdminReqDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXAdminReqMapStruct extends GXBaseMapStruct<GXAdminReqProtocol, GXAdminReqDto> {
}
