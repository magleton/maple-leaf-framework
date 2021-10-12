package com.geoxus.shiro.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.mapper.GXBaseMapper;
import com.geoxus.shiro.builder.GXAdminRoleBuilder;
import com.geoxus.shiro.entities.GXAdminRolesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Mapper
public interface GXAdminRoleMapper extends GXBaseMapper<GXAdminRolesEntity> {
    @SelectProvider(type = GXAdminRoleBuilder.class, method = "getAdminRoles")
    Set<String> getAdminRoles(Dict condition);
}
