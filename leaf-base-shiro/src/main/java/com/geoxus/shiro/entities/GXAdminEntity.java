package com.geoxus.shiro.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.common.annotation.GXFieldComment;
import com.geoxus.core.entity.GXBaseEntity;
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
    @GXFieldComment(zhDesc = "主键ID")
    private Integer id;

    @GXFieldComment(zhDesc = "用户名")
    private String username;

    @GXFieldComment(zhDesc = "密码")
    private String password;

    @GXFieldComment(zhDesc = "电话号码")
    private String phone;

    @GXFieldComment(zhDesc = "电子邮件")
    private String email;

    @GXFieldComment(zhDesc = "昵称")
    private String nickname;

    @GXFieldComment(zhDesc = "扩展数据")
    private String ext;

    @GXFieldComment(zhDesc = "账号状态")
    private Integer status;

    @GXFieldComment(zhDesc = "是否超级管理员")
    private Integer superAdmin;
}
