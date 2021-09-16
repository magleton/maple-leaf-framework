package com.geoxus.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.dto.protocol.req.GXBaseSearchReqProtocol;
import com.geoxus.common.dto.protocol.req.UserReqProtocol;
import com.geoxus.common.dto.protocol.res.GXPaginationProtocol;
import com.geoxus.common.dto.protocol.res.UserResProtocol;
import com.geoxus.sso.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXParseRequestAnnotation;
import com.geoxus.common.util.GXResultUtils;
import com.geoxus.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/index/frontend/")
public class IndexController {
    @Resource
    private UserService userService;

    @GetMapping("index")
    @GXLoginAnnotation
    public GXResultUtils<Dict> index() {
        return GXResultUtils.ok("APP , Hello World");
    }

    @PostMapping("hello")
    public GXResultUtils<Dict> hello(@GXParseRequestAnnotation @Validated UserReqProtocol userReqProtocol) {
        userService.createOrUpdate(userReqProtocol);
        return GXResultUtils.ok("Hello World");
    }

    @GetMapping("get-user-info")
    public GXResultUtils<UserResProtocol> getUserInfo(Long userId) {
        UserResProtocol userInfo = userService.getUserInfo(userId);
        return GXResultUtils.ok(userInfo);
    }

    @GXLoginAnnotation
    @PostMapping("list-or-search")
    public GXResultUtils<GXPaginationProtocol<UserResProtocol>> listOrSearch(@RequestBody GXBaseSearchReqProtocol searchReqProtocol) {
        GXPaginationProtocol<UserResProtocol> pagination = userService.listOrSearchPage(searchReqProtocol);
        return GXResultUtils.ok(pagination);
    }
}
