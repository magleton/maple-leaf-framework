package com.geoxus.core.framework.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.dao.GXCoreConfigDao;
import com.geoxus.core.framework.entity.GXCoreConfigEntity;
import com.geoxus.core.framework.mapper.GXCoreConfigMapper;

public interface GXCoreConfigService extends GXBaseService<GXCoreConfigEntity, GXCoreConfigMapper, GXCoreConfigDao> {
    <T> T getConfigObject(String key, Class<T> clazz);

    boolean updateValueByParamKey(String cloudStorageConfigKey, String toJsonStr);
}
