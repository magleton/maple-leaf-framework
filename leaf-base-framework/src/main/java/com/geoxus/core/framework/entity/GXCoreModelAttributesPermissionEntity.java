package com.geoxus.core.framework.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("core_model_attributes_permission")
@EqualsAndHashCode(callSuper = false)
public class GXCoreModelAttributesPermissionEntity extends GXBaseEntity {
    @TableId
    private Integer attributePermissionId;

    @GXFieldCommentAnnotation(zhDesc = "模型组的ID")
    private Integer modelAttributesId;

    @GXFieldCommentAnnotation(zhDesc = "拒绝的人员或者角色({\"role\":\"1\",\"user\":\"1\"})")
    private String deny;
}
