package cn.maple.shiro.mapper;

import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.shiro.dto.res.GXAdminResDto;
import cn.maple.shiro.entities.GXAdminEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface GXAdminMapper extends GXBaseMapper<GXAdminEntity, GXAdminResDto> {
}
