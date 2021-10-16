package com.geoxus.core.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.mapper.GXBaseMapper;

public class GXBaseDao<M extends GXBaseMapper<T>, T> extends ServiceImpl<M, T> {
}