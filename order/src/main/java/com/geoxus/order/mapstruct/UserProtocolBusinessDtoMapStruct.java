package com.geoxus.order.mapstruct;

import com.geoxus.common.mapstruct.GXBaseMapStruct;
import com.geoxus.order.dto.UserReqBusinessDto;
import com.geoxus.order.dto.protocol.req.UserReqProtocol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProtocolBusinessDtoMapStruct extends GXBaseMapStruct<UserReqProtocol, UserReqBusinessDto> {
}
