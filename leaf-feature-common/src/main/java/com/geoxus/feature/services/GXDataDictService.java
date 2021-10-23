package com.geoxus.feature.services;

import com.geoxus.feature.dao.GXDataDictDao;
import com.geoxus.feature.entities.GXDataDictEntity;
import com.geoxus.feature.mappers.GXDataDictMapper;

public interface GXDataDictService extends com.geoxus.core.datasource.service.GXDBBaseService<GXDataDictEntity, GXDataDictMapper, GXDataDictDao>, com.geoxus.core.datasource.service.GXValidateDBExistsService, com.geoxus.core.datasource.service.GXValidateDBUniqueService {
}