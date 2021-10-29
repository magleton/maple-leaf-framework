package com.geoxus.shiro.mapper;

import com.geoxus.core.datasource.mapper.GXBaseMapper;
import com.geoxus.shiro.dto.res.GXAdminResDto;
import com.geoxus.shiro.entities.GXAdminEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface GXAdminMapper extends GXBaseMapper<GXAdminEntity, GXAdminResDto> {
}
