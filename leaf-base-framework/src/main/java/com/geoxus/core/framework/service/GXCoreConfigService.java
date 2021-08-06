package com.geoxus.core.framework.service;

import com.geoxus.core.framework.entity.GXCoreConfigEntity;

public interface GXCoreConfigService extends GXBaseService<GXCoreConfigEntity> {
    <T> T getConfigObject(String key, Class<T> clazz);

    boolean updateValueByParamKey(String cloudStorageConfigKey, String toJsonStr);
}
