package com.geoxus.common.service;

import com.geoxus.core.service.GXDBBaseService;
import com.geoxus.common.dao.GXCoreConfigDao;
import com.geoxus.common.entity.GXCoreConfigEntity;
import com.geoxus.common.mapper.GXCoreConfigMapper;

public interface GXCoreConfigService extends GXDBBaseService<GXCoreConfigEntity, GXCoreConfigMapper, GXCoreConfigDao> {
    <T> T getConfigObject(String key, Class<T> clazz);

    boolean updateValueByParamKey(String cloudStorageConfigKey, String toJsonStr);
}
