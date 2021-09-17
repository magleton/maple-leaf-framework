package com.geoxus.shiro.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXParseRequestAnnotation;
import com.geoxus.core.common.controller.GXBaseController;
import com.geoxus.common.util.GXResultUtil;
import com.geoxus.shiro.dto.req.GXAdminPermissionsReqDto;
import com.geoxus.shiro.entities.GXAdminPermissionsEntity;
import com.geoxus.shiro.mapstruct.GXAdminPermissionsMapStruct;
import com.geoxus.shiro.service.GXAdminPermissionsService;
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
public class GXAdminPermissionsController implements GXBaseController {
    @Resource
    private GXAdminPermissionsService adminPermissionsService;

    @Resource
    private GXAdminPermissionsMapStruct adminPermissionsMapStruct;

    /**
     * 新增管理员权限
     *
     * @param target 请求参数
     * @return GXResultUtils
     */
    @RequiresRoles("super_admin")
    @PostMapping("create")
    public GXResultUtil<Dict> create(@GXParseRequestAnnotation @Validated GXAdminPermissionsReqDto target) {
        GXAdminPermissionsEntity entity = adminPermissionsMapStruct.dtoToEntity(target);
        long id = adminPermissionsService.create(entity, Dict.create());
        return GXResultUtil.ok(Dict.create().set("id", id));
    }
}
