package com.geoxus.shiro.mapper;

import com.geoxus.core.mapper.GXBaseMapper;
import com.geoxus.shiro.entities.GXAdminEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface GXAdminMapper extends GXBaseMapper<GXAdminEntity> {
}
