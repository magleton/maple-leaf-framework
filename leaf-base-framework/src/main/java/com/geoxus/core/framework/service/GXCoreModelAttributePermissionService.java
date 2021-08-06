package com.geoxus.core.framework.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.entity.GXCoreModelAttributesPermissionEntity;

public interface GXCoreModelAttributePermissionService extends GXBaseService<GXCoreModelAttributesPermissionEntity> {
    /**
     * 通过核心模型Id获取模型属性的权限
     *
     * @param coreModelId 核心模型ID
     * @param param       参数
     * @return List
     */
    Dict getModelAttributePermissionByCoreModelId(int coreModelId, Dict param);
}
