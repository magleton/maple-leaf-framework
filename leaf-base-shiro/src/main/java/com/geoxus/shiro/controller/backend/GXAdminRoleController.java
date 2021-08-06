package com.geoxus.shiro.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToTargetAnnotation;
import com.geoxus.core.common.controller.GXControllerDTO;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.shiro.dto.req.GXAdminRoleReqDto;
import com.geoxus.shiro.entities.GXAdminRoleEntity;
import com.geoxus.shiro.mapstruct.GXAdminRoleMapStruct;
import com.geoxus.shiro.services.GXAdminRoleService;
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
public class GXAdminRoleController implements GXControllerDTO<GXAdminRoleReqDto> {
    @Resource
    private GXAdminRoleService<GXAdminRoleEntity> adminRoleService;

    @Resource
    private GXAdminRoleMapStruct adminRoleMapStruct;

    /**
     * 为管理员新增角色
     *
     * @param target 请求参数
     * @return GXResultUtils
     */
    @Override
    @PostMapping("create")
    public GXResultUtils<Dict> create(@GXRequestBodyToTargetAnnotation @Validated GXAdminRoleReqDto target) {
        GXAdminRoleEntity entity = adminRoleMapStruct.dtoToEntity(target);
        long id = adminRoleService.create(entity, Dict.create());
        return GXResultUtils.ok(Dict.create().set("id", id));
    }
}
