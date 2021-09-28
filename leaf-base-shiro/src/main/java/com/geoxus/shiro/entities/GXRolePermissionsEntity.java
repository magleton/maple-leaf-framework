package com.geoxus.shiro.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.core.entity.GXBaseEntity;
import com.geoxus.shiro.constant.GXRolePermissionsConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName(GXRolePermissionsConstant.TABLE_NAME)
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GXRolePermissionsEntity extends GXBaseEntity implements Serializable {
    @GXFieldComment(zhDesc = "主键ID")
    private Integer id;

    @GXFieldComment(zhDesc = "角色ID")
    private Integer roleId;

    @GXFieldComment(zhDesc = "权限ID")
    private Integer permissionId;

    @GXFieldComment(zhDesc = "扩展数据")
    private String ext;
}
