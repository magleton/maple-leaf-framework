package com.geoxus.shiro.service.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.shiro.dao.GXAdminPermissionsDao;
import com.geoxus.shiro.entities.GXAdminPermissionsEntity;
import com.geoxus.shiro.mapper.GXAdminPermissionsMapper;
import com.geoxus.shiro.service.GXAdminPermissionsService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GXAdminPermissionsServiceImpl extends com.geoxus.core.service.impl.GXDBBaseServiceImpl<GXAdminPermissionsEntity, GXAdminPermissionsMapper, GXAdminPermissionsDao> implements GXAdminPermissionsService, com.geoxus.core.service.GXValidateDBExistsService, com.geoxus.core.service.GXValidateDBUniqueService, com.geoxus.core.service.GXDBBaseService<GXAdminPermissionsEntity, GXAdminPermissionsMapper, GXAdminPermissionsDao> {
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

    public long create(GXAdminPermissionsEntity target, Dict param) {
        baseDao.save(target);
        return target.getId();
    }
}
