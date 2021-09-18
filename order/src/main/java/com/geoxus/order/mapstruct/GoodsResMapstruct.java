package com.geoxus.order.mapstruct;

import com.geoxus.order.dto.protocol.res.GoodsResProtocol;
import com.geoxus.order.dto.res.GoodsResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GoodsResMapstruct {
    @Mapping(source = "goodsName", target = "goodsNameXX")
    GoodsResProtocol dtoToProtocol(GoodsResDto dto);
}
