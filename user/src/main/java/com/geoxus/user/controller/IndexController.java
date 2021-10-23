package com.geoxus.user.controller;

import com.geoxus.sso.annotation.GXLoginAnnotation;
import com.geoxus.core.framework.util.GXResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index/frontend1/")
public class IndexController {
    @GetMapping("index")
    @GXLoginAnnotation
    public GXResultUtil<String> index() {
        return GXResultUtil.ok("SSO , Hello World");
    }
}
