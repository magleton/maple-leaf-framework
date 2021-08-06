package com.geoxus.core.framework.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@TableName("core_model_table_field")
@EqualsAndHashCode(callSuper = false)
@Data
public class GXCoreModelTableFieldEntity extends GXBaseEntity implements Serializable {
    @TableId
    private Integer coreModelDbFieldId;

    private String dbFieldName;

    private String coreModelId;
}
