package cn.maple.core.datasource.service.impl;

import cn.maple.core.datasource.service.GXDBCommonService;
import cn.maple.core.datasource.util.GXDBCommonUtils;
import cn.maple.core.framework.service.impl.GXBusinessServiceImpl;
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
        return GXDBCommonUtils.getTableName(clazz);
    }
}
