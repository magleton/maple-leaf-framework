package com.geoxus.core.framework.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXValidateDBExistsAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@TableName("core_config")
@EqualsAndHashCode(callSuper = false)
public class GXCoreConfigEntity extends GXBaseEntity {
    @TableId
    private Integer configId;

    @GXValidateDBExistsAnnotation
    private Integer coreModelId;

    @NotBlank()
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String paramKey;

    @NotBlank()
    private String paramValue;

    private Integer status;

    private String remark;
}
