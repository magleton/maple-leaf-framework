package com.geoxus.controller;

import com.geoxus.sso.annotation.GXLoginAnnotation;
import com.geoxus.core.common.util.GXResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index/frontend1/")
public class IndexController {
    @GetMapping("index")
    @GXLoginAnnotation
    public GXResultUtils<String> index() {
        return GXResultUtils.ok("SSO , Hello World");
    }
}
