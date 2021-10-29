package com.geoxus.core.datasource.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.datasource.mapper.GXBaseMapper;
import com.geoxus.core.framework.dto.inner.res.GXBaseResDto;

public class GXBaseDao<M extends GXBaseMapper<T, R>, T, R extends GXBaseResDto> extends ServiceImpl<M, T> {
}