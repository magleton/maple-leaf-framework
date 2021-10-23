package com.geoxus.order.mapstruct;

import com.geoxus.core.framework.mapstruct.GXBaseMapStruct;
import com.geoxus.order.dto.protocol.req.UserReqProtocol;
import com.geoxus.order.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProtocolMapstruct extends GXBaseMapStruct<UserReqProtocol, UserEntity> {
}
