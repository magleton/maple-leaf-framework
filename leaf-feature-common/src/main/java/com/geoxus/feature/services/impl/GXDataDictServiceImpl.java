package com.geoxus.feature.services.impl;

import com.geoxus.feature.dao.GXDataDictDao;
import com.geoxus.feature.entities.GXDataDictEntity;
import com.geoxus.feature.mappers.GXDataDictMapper;
import com.geoxus.feature.services.GXDataDictService;
import com.geoxus.core.framework.service.impl.GXBusinessServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class GXDataDictServiceImpl extends GXBusinessServiceImpl<GXDataDictEntity, GXDataDictMapper, GXDataDictDao> implements GXDataDictService {
}
