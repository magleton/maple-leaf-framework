package com.geoxus.shiro.services.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.shiro.entities.GXAdminRoleEntity;
import com.geoxus.shiro.mapper.GXAdminRoleMapper;
import com.geoxus.shiro.services.GXAdminRoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXAdminRoleServiceImpl extends ServiceImpl<GXAdminRoleMapper, GXAdminRoleEntity> implements GXAdminRoleService<GXAdminRoleEntity> {
    @Override
    public Set<String> getAdminRoles(Long adminId) {
        final Dict condition = Dict.create().set("admin_id", adminId);
        return baseMapper.getAdminRoles(condition);
    }

    @Override
    public long create(GXAdminRoleEntity target, Dict param) {
        save(target);
        return target.getId();
    }
}
