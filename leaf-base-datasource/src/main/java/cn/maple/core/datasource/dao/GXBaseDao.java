package cn.maple.core.datasource.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;

public class GXBaseDao<T extends GXBaseEntity, M extends GXBaseMapper<T, R>, R extends GXBaseResDto> extends ServiceImpl<M, T> {
}