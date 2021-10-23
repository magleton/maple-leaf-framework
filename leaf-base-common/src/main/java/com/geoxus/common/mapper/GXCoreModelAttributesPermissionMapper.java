package com.geoxus.common.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.builder.GXCoreModelAttributesPermissionsBuilder;
import com.geoxus.common.entity.GXCoreModelAttributesPermissionEntity;
import com.geoxus.core.datasource.mapper.GXBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper
@Primary
public interface GXCoreModelAttributesPermissionMapper extends GXBaseMapper<GXCoreModelAttributesPermissionEntity> {
    @SelectProvider(type = GXCoreModelAttributesPermissionsBuilder.class, method = "getModelAttributePermissionByCondition")
    List<Dict> getModelAttributePermissionByCondition(Dict param);
}
