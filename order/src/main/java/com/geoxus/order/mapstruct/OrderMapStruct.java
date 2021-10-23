package com.geoxus.order.mapstruct;

import com.geoxus.common.mapstruct.GXBaseMapStruct;
import com.geoxus.order.dto.protocol.res.GoodsResProtocol;
import com.geoxus.order.dto.protocol.res.OrderResProtocol;
import com.geoxus.order.dto.res.GoodsResDto;
import com.geoxus.order.dto.res.OrderResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = GoodsResMapstruct.class)
public interface OrderMapStruct extends GXBaseMapStruct<OrderResDto, OrderResProtocol> {
    /**
     * DTO转换为Entity
     *
     * @param dtoList WEB请求对象
     * @return E
     */
    @Mapping(source = "goodsResDtoList", target = "goodsResProtocols")
    List<OrderResProtocol> dtoListToProtocolList(List<OrderResDto> dtoList);
}
