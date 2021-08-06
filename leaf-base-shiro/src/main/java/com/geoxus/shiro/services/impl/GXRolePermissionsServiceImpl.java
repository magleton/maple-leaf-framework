package com.geoxus.shiro.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.shiro.entities.GXRolePermissionsEntity;
import com.geoxus.shiro.mapper.GXRolePermissionsMapper;
import com.geoxus.shiro.services.GXRolePermissionsService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXRolePermissionsServiceImpl extends ServiceImpl<GXRolePermissionsMapper, GXRolePermissionsEntity> implements GXRolePermissionsService<GXRolePermissionsEntity> {
    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return 权限集
     */
    @Override
    public Set<String> getPermissionsByAdminId(Long adminId) {
        return baseMapper.getPermissionsByAdminId(adminId);
    }
}
