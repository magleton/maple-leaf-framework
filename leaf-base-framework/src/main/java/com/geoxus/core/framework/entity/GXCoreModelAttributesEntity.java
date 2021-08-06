package com.geoxus.core.framework.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.framework.service.GXCoreAttributesService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@TableName("core_model_attributes")
@EqualsAndHashCode(callSuper = false)
public class GXCoreModelAttributesEntity extends GXBaseEntity implements Serializable {
    @TableId("model_attributes_id")
    private Integer modelAttributesId;

    private String dbFieldName;

    private int coreModelId;

    @GXValidateDBExistsAnnotation(service = GXCoreAttributesService.class, fieldName = "attribute_id")
    private int attributeId;

    private String showName;

    private int required;

    private String validationExpression;

    private int forceValidation;

    private String defaultValue;

    private String ext;
}
