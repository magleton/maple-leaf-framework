package com.geoxus.shiro.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToTargetAnnotation;
import com.geoxus.core.common.controller.GXControllerDTO;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.shiro.dto.req.GXAdminPermissionsReqDto;
import com.geoxus.shiro.entities.GXAdminPermissionsEntity;
import com.geoxus.shiro.mapstruct.GXAdminPermissionsMapStruct;
import com.geoxus.shiro.services.GXAdminPermissionsService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 管理员权限管理
 */
@RestController
@RequestMapping("/admin-permissions/backend")
public class GXAdminPermissionsController implements GXControllerDTO<GXAdminPermissionsReqDto> {
    @Resource
    private GXAdminPermissionsService<GXAdminPermissionsEntity> adminPermissionsService;

    @Resource
    private GXAdminPermissionsMapStruct adminPermissionsMapStruct;

    /**
     * 新增管理员权限
     *
     * @param target 请求参数
     * @return GXResultUtils
     */
    @Override
    @RequiresRoles("super_admin")
    @PostMapping("create")
    public GXResultUtils<Dict> create(@GXRequestBodyToTargetAnnotation @Validated GXAdminPermissionsReqDto target) {
        GXAdminPermissionsEntity entity = adminPermissionsMapStruct.dtoToEntity(target);
        long id = adminPermissionsService.create(entity, Dict.create());
        return GXResultUtils.ok(Dict.create().set("id", id));
    }
}
