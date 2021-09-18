package com.geoxus.core.framework.service;

import com.geoxus.core.common.validator.GXValidateDBExistsService;
import com.geoxus.core.framework.dao.GXCoreAttributeEnumsDao;
import com.geoxus.core.framework.entity.GXCoreAttributesEnumsEntity;
import com.geoxus.core.framework.mapper.GXCoreAttributeEnumsMapper;

public interface GXCoreAttributeEnumsService extends GXBaseService<GXCoreAttributesEnumsEntity, GXCoreAttributeEnumsMapper, GXCoreAttributeEnumsDao>, GXValidateDBExistsService {
    /**
     * 检测属性的值是否存在
     *
     * @param attributeId 属性ID
     * @param coreModelId 核心模型ID
     * @param value       　属性值
     * @return　　boolean
     */
    boolean isExistsAttributeValue(int attributeId, Object value, int coreModelId);
}
