package com.geoxus.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.dto.protocol.req.GXBaseSearchReqProtocol;
import com.geoxus.common.dto.protocol.req.UserReqProtocol;
import com.geoxus.common.dto.protocol.res.GXPaginationProtocol;
import com.geoxus.common.dto.protocol.res.UserResProtocol;
import com.geoxus.sso.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXParseRequestAnnotation;
import com.geoxus.common.util.GXResultUtil;
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
    public GXResultUtil<Dict> index() {
        return GXResultUtil.ok("APP , Hello World");
    }

    @PostMapping("hello")
    public GXResultUtil<Dict> hello(@GXParseRequestAnnotation @Validated UserReqProtocol userReqProtocol) {
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
}
