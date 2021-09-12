package com.geoxus.shiro.service.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.core.common.service.impl.GXBusinessServiceImpl;
import com.geoxus.shiro.dao.GXRolePermissionsDao;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;
import com.geoxus.shiro.mapper.GXRolePermissionsMapper;
import com.geoxus.shiro.service.GXRolePermissionsService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXRolePermissionsServiceImpl extends GXBusinessServiceImpl<GXRolePermissionsEntity, GXRolePermissionsMapper, GXRolePermissionsDao, Dict> implements GXRolePermissionsService {
    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return 权限集
     */
    @Override
    public Set<String> getPermissionsByAdminId(Long adminId) {
        return getBaseMapper().getPermissionsByAdminId(adminId);
    }
}
