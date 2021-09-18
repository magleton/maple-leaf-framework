package com.geoxus.order.dto.protocol.res;

import lombok.Data;

import java.util.List;

@Data
public class OrderResProtocol {
    private String orderSn;

    private String totalPrice;

    private List<GoodsResProtocol> goodsResProtocols;
}
