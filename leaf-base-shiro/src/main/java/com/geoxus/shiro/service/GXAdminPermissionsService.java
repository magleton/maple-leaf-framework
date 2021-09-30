package com.geoxus.shiro.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.shiro.dao.GXAdminPermissionsDao;
import com.geoxus.shiro.entities.GXAdminPermissionsEntity;
import com.geoxus.shiro.mapper.GXAdminPermissionsMapper;

import java.util.Set;

public interface GXAdminPermissionsService extends com.geoxus.core.service.GXDBBaseService<GXAdminPermissionsEntity, GXAdminPermissionsMapper, GXAdminPermissionsDao>, com.geoxus.core.service.GXValidateDBExistsService, com.geoxus.core.service.GXValidateDBUniqueService {
    /**
     * 通过管理员ID获取权限集
     *
     * @param adminId 管理员ID
     * @return 权限集
     */
    Set<String> getPermissionsByAdminId(Long adminId);

    /**
     * 创建数据
     *
     * @param target 目标实体
     * @param param  额外参数
     * @return long
     */
    long create(GXAdminPermissionsEntity target, Dict param);
}
