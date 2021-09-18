package com.geoxus.order.mapstruct;

import com.geoxus.order.dto.protocol.res.OrderResProtocol;
import com.geoxus.order.dto.res.OrderResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", uses = GoodsResMapstruct.class)
@Component
public interface OrderMapStruct {
    /**
     * DTO转换为Entity
     *
     * @param dto WEB请求对象
     * @return E
     */
    @Mapping(source = "goodsResDtoList", target = "goodsResProtocols")
    OrderResProtocol dtoToProtocol(OrderResDto dto);

    /**
     * DTO转换为Entity
     *
     * @param dtoList WEB请求对象
     * @return E
     */
    @Mapping(source = "goodsResDtoList", target = "goodsResProtocols")
    List<OrderResProtocol> dtoListToProtocolList(List<OrderResDto> dtoList);
}
