package com.geoxus.core.framework.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.dao.GXCoreModelTableFieldDao;
import com.geoxus.core.framework.entity.GXCoreModelTableFieldEntity;
import com.geoxus.core.framework.mapper.GXCoreModelTableFieldMapper;

import java.util.List;

public interface GXCoreModelTableFieldService extends GXBusinessService<GXCoreModelTableFieldEntity, GXCoreModelTableFieldMapper, GXCoreModelTableFieldDao> {
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
