package cn.maple.shiro.controller.backend;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.annotation.GXRequestBody;
import cn.maple.core.framework.controller.GXBaseController;
import cn.maple.core.framework.util.GXResultUtils;
import cn.maple.shiro.dto.req.GXAdminRolesReqDto;
import cn.maple.shiro.entities.GXAdminRolesEntity;
import cn.maple.shiro.mapstruct.req.GXAdminRoleReqMapStruct;
import cn.maple.shiro.service.GXAdminRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 管理员角色管理
 */
@RestController
@RequestMapping("/admin-role/backend")
public class GXAdminRoleController implements GXBaseController {
    @Resource
    private GXAdminRoleService adminRoleService;

    @Resource
    private GXAdminRoleReqMapStruct adminRoleReqMapStruct;

    /**
     * 为管理员新增角色
     *
     * @param source 请求参数
     * @return GXResultUtils
     */
    @PostMapping("create")
    public GXResultUtils<Dict> create(@GXRequestBody @Validated GXAdminRolesReqDto source) {
        GXAdminRolesEntity entity = adminRoleReqMapStruct.sourceToTarget(source);
        long id = adminRoleService.create(entity, Dict.create());
        return GXResultUtils.ok(Dict.create().set("id", id));
    }
}
