package cn.maple.shiro.mapper;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.shiro.builder.GXAdminRoleBuilder;
import cn.maple.shiro.dto.res.GXAdminRoleResDto;
import cn.maple.shiro.entities.GXAdminRolesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Mapper
public interface GXAdminRoleMapper extends GXBaseMapper<GXAdminRolesEntity, GXAdminRoleResDto> {
    @SelectProvider(type = GXAdminRoleBuilder.class, method = "getAdminRoles")
    Set<String> getAdminRoles(Dict condition);
}
