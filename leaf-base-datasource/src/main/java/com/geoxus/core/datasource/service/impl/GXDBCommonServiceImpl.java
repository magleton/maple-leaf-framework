package com.geoxus.core.datasource.service.impl;

import com.geoxus.core.datasource.service.GXDBCommonService;
import com.geoxus.core.datasource.util.GXDataSourceCommonUtils;
import com.geoxus.core.framework.service.impl.GXBusinessServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class GXDBCommonServiceImpl extends GXBusinessServiceImpl implements GXDBCommonService {
    /**
     * 获取实体的表明
     *
     * @param clazz Class对象
     * @return String
     */
    @Override
    public <T> String getTableName(Class<T> clazz) {
        return GXDataSourceCommonUtils.getTableName(clazz);
    }
}
