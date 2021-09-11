package com.geoxus.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToTargetAnnotation;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.dto.protocol.req.UserReqProtocol;
import com.geoxus.dto.protocol.res.UserResProtocol;
import com.geoxus.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;
import java.util.TreeSet;

@RestController
public class IndexController {
    @Resource
    private UserService userService;

    @PostMapping("/hello")
    public GXResultUtils<Dict> hello(@GXRequestBodyToTargetAnnotation @Validated UserReqProtocol userReqProtocol) {
        Set<String> strings = new TreeSet<>();
        strings.add("MMMM");
        strings.add("KKKK");
        strings.add("AAAA");
        strings.add("BBBB");
        strings.add("KKKKK");
        System.out.println(strings);
        return GXResultUtils.ok("Hello World");
    }

    @GetMapping("get-user-info")
    public GXResultUtils<UserResProtocol> getUserInfo(Long userId) {
        UserResProtocol userInfo = userService.getUserInfo(userId);
        return GXResultUtils.ok(userInfo);
    }
}
