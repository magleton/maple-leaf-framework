package com.geoxus.core.framework.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@TableName("core_attributes_enums")
@EqualsAndHashCode(callSuper = false)
public class GXCoreAttributesEnumsEntity extends GXBaseEntity implements Serializable {
    @TableId
    private Integer attributeEnumId;

    private Integer attributeId;

    private Integer coreModelId;

    private String valueEnum;

    private String showName;
}
