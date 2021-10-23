package com.geoxus.order.mapper;

import com.geoxus.core.datasource.mapper.GXBaseMapper;
import com.geoxus.order.builder.AddressBuilder;
import com.geoxus.order.builder.dto.AddressDto;
import com.geoxus.order.dto.protocol.res.UserResProtocol;
import com.geoxus.order.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AddressMapper extends GXBaseMapper<UserEntity> {
    @SelectProvider(type = AddressBuilder.class, method = "getListByCondition")
    UserResProtocol getListByCondition(AddressDto dto);
}
