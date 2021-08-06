package com.geoxus.shiro.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.shiro.constant.GXAdminConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName(GXAdminConstant.TABLE_NAME)
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GXAdminEntity extends GXBaseEntity implements Serializable {
    @GXFieldCommentAnnotation(zhDesc = "主键ID")
    private Integer id;

    @GXFieldCommentAnnotation(zhDesc = "用户名")
    private String username;

    @GXFieldCommentAnnotation(zhDesc = "密码")
    private String password;

    @GXFieldCommentAnnotation(zhDesc = "电话号码")
    private String phone;

    @GXFieldCommentAnnotation(zhDesc = "电子邮件")
    private String email;

    @GXFieldCommentAnnotation(zhDesc = "昵称")
    private String nickname;

    @GXFieldCommentAnnotation(zhDesc = "扩展数据")
    private String ext;

    @GXFieldCommentAnnotation(zhDesc = "账号状态")
    private Integer status;

    @GXFieldCommentAnnotation(zhDesc = "是否超级管理员")
    private Integer superAdmin;
}
