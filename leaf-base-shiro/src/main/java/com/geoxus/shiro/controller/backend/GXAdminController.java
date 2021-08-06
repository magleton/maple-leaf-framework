package com.geoxus.shiro.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.controller.GXControllerDTO;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.shiro.dto.req.GXAdminLoginReqDto;
import com.geoxus.shiro.dto.req.GXAdminReqDto;
import com.geoxus.shiro.entities.GXAdminEntity;
import com.geoxus.shiro.services.GXAdminService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 管理员管理
 */
@RestController
@RequestMapping("/admin/backend")
public class GXAdminController implements GXControllerDTO<GXAdminReqDto> {
    @Resource
    private GXAdminService<GXAdminEntity> adminService;

    /**
     * 管理员登录
     *
     * @param loginReqDto 登录信息
     * @return GXResultUtils
     */
    @PostMapping("login")
    public GXResultUtils<Dict> login(@RequestBody @Validated GXAdminLoginReqDto loginReqDto) {
        final String token = adminService.login(loginReqDto);
        return GXResultUtils.ok(Dict.create().set("admin-token", token).set("username", loginReqDto.getUsername()));
    }
}
