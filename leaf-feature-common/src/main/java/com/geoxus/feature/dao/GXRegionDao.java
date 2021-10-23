package com.geoxus.feature.dao;

import com.geoxus.feature.entities.GXRegionEntity;
import com.geoxus.feature.mappers.GXRegionMapper;
import com.geoxus.core.datasource.dao.GXBaseDao;
import org.springframework.stereotype.Repository;

@Repository
public class GXRegionDao extends GXBaseDao<GXRegionMapper, GXRegionEntity> {
}
