package cn.maple.shiro.mapper;

import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.shiro.dto.res.GXPermissionsResDto;
import cn.maple.shiro.entities.GXPermissionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface GXPermissionsMapper extends GXBaseMapper<GXPermissionsEntity, GXPermissionsResDto> {
}
