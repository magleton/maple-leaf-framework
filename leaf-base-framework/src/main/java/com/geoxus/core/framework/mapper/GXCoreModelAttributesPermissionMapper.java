package com.geoxus.core.framework.mapper;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.builder.GXCoreModelAttributesPermissionsBuilder;
import com.geoxus.core.framework.entity.GXCoreModelAttributesPermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper
@Primary
public interface GXCoreModelAttributesPermissionMapper extends GXBaseMapper<GXCoreModelAttributesPermissionEntity> {
    @SelectProvider(type = GXCoreModelAttributesPermissionsBuilder.class, method = "getModelAttributePermissionByModelId")
    List<Dict> getModelAttributePermissionByModelId(Dict param);
}
