package com.geoxus.core.framework.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.core.framework.entity.GXCoreModelTableFieldEntity;

import java.util.List;

public interface GXCoreModelTableFieldService extends GXBusinessService<GXCoreModelTableFieldEntity> {
    /**
     * 通过条件获取数据
     *
     * @param condition 分类名字
     * @return List
     */
    GXCoreModelTableFieldEntity getCoreModelDbFieldByCondition(Dict condition);

    /**
     * 通过条件获取数据
     *
     * @param condition 查询条件
     * @return List
     */
    List<Dict> getModelAttributesByModelId(Dict condition);
}
