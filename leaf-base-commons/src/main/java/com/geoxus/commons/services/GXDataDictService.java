package com.geoxus.commons.services;

import cn.hutool.core.lang.Dict;
import com.geoxus.commons.dao.GXDataDictDao;
import com.geoxus.commons.entities.GXDataDictEntity;
import com.geoxus.commons.mappers.GXDataDictMapper;
import com.geoxus.core.common.service.GXBusinessService;

public interface GXDataDictService extends GXBusinessService<GXDataDictEntity, GXDataDictMapper, GXDataDictDao, Dict> {
}