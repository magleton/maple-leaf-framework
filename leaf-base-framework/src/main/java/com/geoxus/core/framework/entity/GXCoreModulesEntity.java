package com.geoxus.core.framework.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@TableName("core_modules")
@EqualsAndHashCode(callSuper = false)
public class GXCoreModulesEntity extends GXBaseEntity implements Serializable {
    @TableId
    private int moduleId;

    private int parentId;

    private String moduleName;
}
