package com.geoxus.shiro.mapper;

import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.shiro.entities.GXPermissionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface GXPermissionsMapper extends GXBaseMapper<GXPermissionsEntity> {
}
