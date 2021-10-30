package cn.maple.core.datasource.service;

import cn.maple.core.framework.service.GXBusinessService;

public interface GXDBCommonService extends GXBusinessService {
    /**
     * 获取实体的表明
     *
     * @param clazz Class对象
     * @return String
     */
    <T> String getTableName(Class<T> clazz);
}
