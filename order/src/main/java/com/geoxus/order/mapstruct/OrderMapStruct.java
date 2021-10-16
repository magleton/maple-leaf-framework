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

    /**
     * 自定义方法
     * 调用规则: 方法签名一致即可被Mapstruct自动调用 (Ljava/util/List;)Ljava/util/List;
     * call rule : The generated code will invoke the default methods if the argument and return types match
     *
     * @param goodsResDtoList 商品列表
     * @return List
     */
    default List<GoodsResProtocol> goodsResListProtocol(List<GoodsResDto> goodsResDtoList) {
        List<GoodsResProtocol> list = new ArrayList<>();
        goodsResDtoList.forEach(goodsResDto -> {
            GoodsResProtocol protocol = new GoodsResProtocol();
            protocol.setGoodsPrice(goodsResDto.getGoodsPrice());
            protocol.setGoodsNameXX(goodsResDto.getGoodsName());
            list.add(protocol);
        });
        return list;
    }
}
