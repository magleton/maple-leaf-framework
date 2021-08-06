package com.geoxus.core.framework.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.validator.GXValidateDBExists;
import com.geoxus.core.framework.entity.GXCoreAttributesEnumsEntity;

import java.util.List;

public interface GXCoreAttributeEnumsService extends GXBaseService<GXCoreAttributesEnumsEntity>, GXValidateDBExists {
    /**
     * 检测属性的值是否存在
     *
     * @param attributeId 属性ID
     * @param coreModelId 核心模型ID
     * @param value       　属性值
     * @return　　boolean
     */
    boolean isExistsAttributeValue(int attributeId, Object value, int coreModelId);

    /**
     * 根据属性名字获取属性的枚举列表
     *
     * @param condition 　条件
     * @return　List
     */
    List<Dict> getAttributeEnumsByCondition(Dict condition);
}
