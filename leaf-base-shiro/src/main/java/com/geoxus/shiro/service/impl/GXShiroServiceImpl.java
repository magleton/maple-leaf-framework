package com.geoxus.shiro.service.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.shiro.service.GXAdminRoleService;
import com.geoxus.shiro.service.GXAdminService;
import com.geoxus.shiro.service.GXPermissionsService;
import com.geoxus.shiro.service.GXShiroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class GXShiroServiceImpl implements GXShiroService {
    /**
     * 获取用户权限列表
     *
     * @param adminId 管理员ID
     */
    @SuppressWarnings(value = {"unchecked"})
    public Set<String> getAdminAllPermissions(Long adminId) {
        return Objects.requireNonNull(GXSpringContextUtil.getBean(GXPermissionsService.class)).getAdminAllPermissions(adminId);
    }

    /**
     * 获取用户角色列表
     *
     * @return Dict
     */
    public Set<String> getAdminRoles(Long adminId) {
        return Objects.requireNonNull(GXSpringContextUtil.getBean(GXAdminRoleService.class)).getAdminRoles(adminId);
    }

    @Override
    public Dict getAdminById(Long adminId) {
        final Dict dict = Objects.requireNonNull(GXSpringContextUtil.getBean(GXAdminService.class)).getStatus(adminId);
        if (null == dict) {
            return Dict.create();
        }
        return dict;
    }

    @Override
    public boolean isSuperAdmin(Dict adminData) {
        final String primaryKey = Objects.requireNonNull(GXSpringContextUtil.getBean(GXAdminService.class)).getPrimaryKey();
        if (null != adminData.getLong(primaryKey)) {
            return adminData.getLong(primaryKey).equals(GXCommonUtils.getEnvironmentValue("super.admin.id", Long.class));
        }
        return adminData.getLong("super_admin") == 1;
    }

    @Override
    public Long currentSessionUserId() {
        return 100L;
    }

    @Override
    public Dict adminRoles(Long currentAdminId) {
        return Dict.create();
    }
}
