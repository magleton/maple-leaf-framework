package com.geoxus.shiro.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.shiro.constant.GXAdminRoleConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName(GXAdminRoleConstant.TABLE_NAME)
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GXAdminRoleEntity extends GXBaseEntity implements Serializable {
    @GXFieldCommentAnnotation(zhDesc = "主键ID")
    private Integer id;

    @GXFieldCommentAnnotation(zhDesc = "管理员ID")
    private Integer adminId;

    @GXFieldCommentAnnotation(zhDesc = "角色ID")
    private Integer roleId;

    @GXFieldCommentAnnotation(zhDesc = "扩展数据")
    private String ext;
}
