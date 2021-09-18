package com.geoxus.order.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.annotation.GXRequestBody;
import com.geoxus.common.dto.protocol.req.GXBaseSearchReqProtocol;
import com.geoxus.common.dto.protocol.res.GXPaginationProtocol;
import com.geoxus.common.util.GXResultUtil;
import com.geoxus.order.common.dto.protocol.req.UserReqProtocol;
import com.geoxus.order.common.dto.protocol.res.UserResProtocol;
import com.geoxus.order.dto.protocol.res.OrderResProtocol;
import com.geoxus.order.dto.res.GoodsResDto;
import com.geoxus.order.dto.res.OrderResDto;
import com.geoxus.order.mapstruct.OrderMapStruct;
import com.geoxus.order.service.UserService;
import com.geoxus.sso.annotation.GXLoginAnnotation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/index/frontend/")
public class IndexController {
    @Resource
    private UserService userService;

    @Resource
    private OrderMapStruct orderMapStruct;

    @GetMapping("index")
    @GXLoginAnnotation
    public GXResultUtil<Dict> index() {
        return GXResultUtil.ok("APP , Hello World");
    }

    @PostMapping("hello")
    public GXResultUtil<Dict> hello(@GXRequestBody @Validated UserReqProtocol userReqProtocol) {
        userService.createOrUpdate(userReqProtocol);
        return GXResultUtil.ok("Hello World");
    }

    @GetMapping("get-user-info")
    public GXResultUtil<UserResProtocol> getUserInfo(Long userId) {
        UserResProtocol userInfo = userService.getUserInfo(userId);
        return GXResultUtil.ok(userInfo);
    }

    @GXLoginAnnotation
    @PostMapping("list-or-search")
    public GXResultUtil<GXPaginationProtocol<UserResProtocol>> listOrSearch(@RequestBody GXBaseSearchReqProtocol searchReqProtocol) {
        GXPaginationProtocol<UserResProtocol> pagination = userService.listOrSearchPage(searchReqProtocol);
        return GXResultUtil.ok(pagination);
    }

    @GetMapping("test")
    public GXResultUtil<List<OrderResProtocol>> test() {
        GoodsResDto g1 = GoodsResDto.builder().goodsPrice("19.9").goodsName("香皂").build();
        GoodsResDto g2 = GoodsResDto.builder().goodsPrice("29.9").goodsName("肥皂").build();
        OrderResDto orderResDto1 = OrderResDto.builder()
                .orderSn("10")
                .totalPrice("109.9")
                .goodsResDtoList(Arrays.asList(g1, g2)).build();
        GoodsResDto g3 = GoodsResDto.builder().goodsPrice("39.9").goodsName("沐浴露").build();
        GoodsResDto g4 = GoodsResDto.builder().goodsPrice("49.9").goodsName("电脑").build();
        OrderResDto orderResDto2 = OrderResDto.builder()
                .orderSn("100")
                .totalPrice("1009.9")
                .goodsResDtoList(Arrays.asList(g3, g4)).build();
        List<OrderResProtocol> orderResProtocols = orderMapStruct.dtoListToProtocolList(Arrays.asList(orderResDto1, orderResDto2));
        return GXResultUtil.ok(orderResProtocols);
    }
}
