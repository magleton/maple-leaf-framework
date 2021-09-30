package com.geoxus.shiro.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.shiro.builder.GXAdminRoleBuilder;
import com.geoxus.shiro.entities.GXAdminRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Mapper
public interface GXAdminRoleMapper extends GXFrameworkBaseMapper<GXAdminRoleEntity> {
    @SelectProvider(type = GXAdminRoleBuilder.class, method = "getAdminRoles")
    Set<String> getAdminRoles(Dict condition);
}
