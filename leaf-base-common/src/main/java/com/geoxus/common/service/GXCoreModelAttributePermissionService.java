package com.geoxus.common.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.service.GXDBBaseService;
import com.geoxus.common.dao.GXCoreModelAttributesPermissionDao;
import com.geoxus.common.entity.GXCoreModelAttributesPermissionEntity;
import com.geoxus.common.mapper.GXCoreModelAttributesPermissionMapper;

public interface GXCoreModelAttributePermissionService extends GXDBBaseService<GXCoreModelAttributesPermissionEntity, GXCoreModelAttributesPermissionMapper, GXCoreModelAttributesPermissionDao> {
    /**
     * 通过核心模型Id获取模型属性的权限
     *
     * @param coreModelId 核心模型ID
     * @param param       参数
     * @return List
     */
    Dict getModelAttributePermissionByCoreModelId(int coreModelId, Dict param);
}
