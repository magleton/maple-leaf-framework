package com.geoxus.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToTargetAnnotation;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.dto.protocol.req.UserReqProtocol;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @PostMapping("/hello")
    public GXResultUtils<Dict> hello(@GXRequestBodyToTargetAnnotation @Validated UserReqProtocol userReqProtocol) {
        return GXResultUtils.ok("Hello World");
    }
}
