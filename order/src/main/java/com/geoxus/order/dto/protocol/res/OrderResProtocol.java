package com.geoxus.order.dto.protocol.res;

import com.geoxus.common.dto.GXBaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderResProtocol extends GXBaseData {
    private String orderSn;

    private String totalPrice;

    private transient List<GoodsResProtocol> goodsResProtocols;
}
