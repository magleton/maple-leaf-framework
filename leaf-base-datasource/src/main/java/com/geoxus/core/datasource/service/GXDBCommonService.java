package com.geoxus.core.datasource.service;

import com.geoxus.core.framework.service.GXBusinessService;

public interface GXDBCommonService extends GXBusinessService {
    /**
     * 获取实体的表明
     *
     * @param clazz Class对象
     * @return String
     */
    <T> String getTableName(Class<T> clazz);
}
