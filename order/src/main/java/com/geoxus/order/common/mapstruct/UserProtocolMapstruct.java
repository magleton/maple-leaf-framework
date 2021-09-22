package com.geoxus.order.common.mapstruct;

import com.geoxus.common.mapstruct.GXBaseMapStruct;
import com.geoxus.order.common.dto.protocol.req.UserReqProtocol;
import com.geoxus.order.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProtocolMapstruct extends GXBaseMapStruct<UserReqProtocol, UserEntity> {
    UserEntity dtoToEntity(UserReqProtocol reqProtocol);
}
