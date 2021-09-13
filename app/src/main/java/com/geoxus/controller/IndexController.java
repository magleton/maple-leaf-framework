package com.geoxus.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToTargetAnnotation;
import com.geoxus.dto.protocol.req.GXBaseSearchReqProtocol;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.dto.protocol.req.UserReqProtocol;
import com.geoxus.dto.protocol.res.UserResProtocol;
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
    public GXResultUtils<Dict> hello(@GXRequestBodyToTargetAnnotation @Validated UserReqProtocol userReqProtocol) {
        userService.createOrUpdate(userReqProtocol);
        return GXResultUtils.ok("Hello World");
    }

    @GetMapping("get-user-info")
    public GXResultUtils<UserResProtocol> getUserInfo(Long userId) {
        UserResProtocol userInfo = userService.getUserInfo(userId);
        return GXResultUtils.ok(userInfo);
    }

    @PostMapping("list-or-search")
    public GXResultUtils<GXPagination<UserResProtocol>> listOrSearch(@RequestBody GXBaseSearchReqProtocol searchReqProtocol) {
        GXPagination<UserResProtocol> pagination = userService.listOrSearchPage(searchReqProtocol, UserResProtocol.class);
        GXPagination<UserResProtocol> pagination1 = userService.listOrSearchPage(searchReqProtocol, UserResProtocol.class);
        return GXResultUtils.ok(pagination);
    }
}
