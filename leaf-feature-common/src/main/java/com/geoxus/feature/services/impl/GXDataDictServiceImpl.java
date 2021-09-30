package com.geoxus.feature.services.impl;

import com.geoxus.feature.dao.GXDataDictDao;
import com.geoxus.feature.entities.GXDataDictEntity;
import com.geoxus.feature.mappers.GXDataDictMapper;
import com.geoxus.feature.services.GXDataDictService;
import org.springframework.stereotype.Service;

@Service
public class GXDataDictServiceImpl extends com.geoxus.core.service.impl.GXDBBaseServiceImpl<GXDataDictEntity, GXDataDictMapper, GXDataDictDao> implements GXDataDictService, com.geoxus.core.service.GXValidateDBExistsService, com.geoxus.core.service.GXValidateDBUniqueService, com.geoxus.core.service.GXDBBaseService<GXDataDictEntity, GXDataDictMapper, GXDataDictDao> {
}
