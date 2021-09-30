package com.geoxus.shiro.mapper;

import com.geoxus.shiro.entities.GXPermissionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface GXPermissionsMapper extends GXFrameworkBaseMapper<GXPermissionsEntity> {
}
