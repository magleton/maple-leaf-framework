package com.geoxus.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.dto.protocol.req.GXBaseSearchReqProtocol;
import com.geoxus.common.dto.protocol.req.UserReqProtocol;
import com.geoxus.common.dto.protocol.res.UserResProtocol;
import com.geoxus.common.dto.protocol.res.GXPaginationProtocol;
import com.geoxus.core.common.annotation.GXParseRequestAnnotation;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class IndexController {
    @Resource
    private UserService userService;

    @PostMapping("/hello")
    public GXResultUtils<Dict> hello(@GXParseRequestAnnotation @Validated UserReqProtocol userReqProtocol) {
        userService.createOrUpdate(userReqProtocol);
        return GXResultUtils.ok("Hello World");
    }

    @GetMapping("get-user-info")
    public GXResultUtils<UserResProtocol> getUserInfo(Long userId) {
        UserResProtocol userInfo = userService.getUserInfo(userId);
        return GXResultUtils.ok(userInfo);
    }

    @PostMapping("list-or-search")
    public GXResultUtils<GXPaginationProtocol<UserResProtocol>> listOrSearch(@RequestBody GXBaseSearchReqProtocol searchReqProtocol) {
        GXPaginationProtocol<UserResProtocol> pagination = userService.listOrSearchPage(searchReqProtocol, UserResProtocol.class);
        GXPaginationProtocol<UserResProtocol> pagination1 = userService.listOrSearchPage(searchReqProtocol, UserResProtocol.class);
        return GXResultUtils.ok(pagination);
    }
}
