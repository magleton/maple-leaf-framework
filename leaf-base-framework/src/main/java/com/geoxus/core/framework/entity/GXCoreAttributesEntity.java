package com.geoxus.core.framework.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@TableName("core_attributes")
@EqualsAndHashCode(callSuper = false)
@Data
public class GXCoreAttributesEntity extends GXBaseEntity implements Serializable {
    @TableId
    private Integer attributeId;

    private String category;

    private String attributeName;

    private String showName;

    private String validationDesc;

    private String validationExpression;

    private String ext;

    private Integer isCore;

    private String dataType;

    private String columnType;

    private String frontType;
}
