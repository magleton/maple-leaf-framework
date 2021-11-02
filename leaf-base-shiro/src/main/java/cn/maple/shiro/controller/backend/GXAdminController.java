package cn.maple.shiro.controller.backend;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.annotation.GXRequestBody;
import cn.maple.core.framework.controller.GXBaseController;
import cn.maple.core.framework.util.GXResultUtils;
import cn.maple.shiro.dto.req.GXAdminLoginReqDto;
import cn.maple.shiro.service.GXAdminService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 管理员管理
 */
@RestController
@RequestMapping("/admin/backend")
public class GXAdminController implements GXBaseController {
    @Resource
    private GXAdminService adminService;


    /**
     * 管理员登录
     *
     * @param loginReqDto 登录信息
     * @return GXResultUtils
     */
    @PostMapping("login")
    public GXResultUtils<Dict> login(@GXRequestBody @Validated GXAdminLoginReqDto loginReqDto) {
        final String token = adminService.login(loginReqDto);
        return GXResultUtils.ok(Dict.create().set("admin-token", token).set("username", loginReqDto.getUsername()));
    }
}
