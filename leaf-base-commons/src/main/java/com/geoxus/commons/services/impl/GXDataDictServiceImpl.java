package com.geoxus.commons.services.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.commons.dao.GXDataDictDao;
import com.geoxus.commons.entities.GXDataDictEntity;
import com.geoxus.commons.mappers.GXDataDictMapper;
import com.geoxus.commons.services.GXDataDictService;
import com.geoxus.core.common.service.impl.GXBusinessServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class GXDataDictServiceImpl extends GXBusinessServiceImpl<GXDataDictEntity, GXDataDictMapper, GXDataDictDao, Dict> implements GXDataDictService {
}
