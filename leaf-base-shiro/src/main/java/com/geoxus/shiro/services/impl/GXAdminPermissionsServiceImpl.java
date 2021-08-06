package com.geoxus.shiro.services.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.shiro.entities.GXAdminPermissionsEntity;
import com.geoxus.shiro.mapper.GXAdminPermissionsMapper;
import com.geoxus.shiro.services.GXAdminPermissionsService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXAdminPermissionsServiceImpl extends ServiceImpl<GXAdminPermissionsMapper, GXAdminPermissionsEntity> implements GXAdminPermissionsService<GXAdminPermissionsEntity> {
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

    @Override
    public long create(GXAdminPermissionsEntity target, Dict param) {
        save(target);
        return target.getId();
    }
}
