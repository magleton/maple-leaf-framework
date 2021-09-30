package com.geoxus.feature.services;

import com.geoxus.feature.dao.GXDataDictDao;
import com.geoxus.feature.entities.GXDataDictEntity;
import com.geoxus.feature.mappers.GXDataDictMapper;

public interface GXDataDictService extends com.geoxus.core.service.GXDBBaseService<GXDataDictEntity, GXDataDictMapper, GXDataDictDao>, com.geoxus.core.service.GXValidateDBExistsService, com.geoxus.core.service.GXValidateDBUniqueService {
}