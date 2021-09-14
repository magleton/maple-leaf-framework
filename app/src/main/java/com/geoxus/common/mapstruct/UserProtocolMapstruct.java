package com.geoxus.common.mapstruct;

import com.geoxus.common.dto.protocol.req.UserReqProtocol;
import com.geoxus.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProtocolMapstruct extends GXBaseMapStruct<UserReqProtocol, UserEntity> {
}
