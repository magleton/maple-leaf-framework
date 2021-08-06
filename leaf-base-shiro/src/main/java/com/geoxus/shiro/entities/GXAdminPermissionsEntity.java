package com.geoxus.shiro.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.shiro.constant.GXAdminPermissionsConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName(GXAdminPermissionsConstant.TABLE_NAME)
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GXAdminPermissionsEntity extends GXBaseEntity implements Serializable {
    @GXFieldCommentAnnotation(zhDesc = "主键ID")
    private Integer id;

    @GXFieldCommentAnnotation(zhDesc = "管理员ID")
    private Integer adminId;

    @GXFieldCommentAnnotation(zhDesc = "权限ID")
    private Integer permissionId;

    @GXFieldCommentAnnotation(zhDesc = "扩展数据")
    private String ext;
}
