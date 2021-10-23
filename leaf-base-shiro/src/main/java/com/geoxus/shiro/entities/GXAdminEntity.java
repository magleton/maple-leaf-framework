package com.geoxus.shiro.entities;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实名字
     */
    private String realName;

    /**
     * 账号状态
     */
    private Integer status;

    /**
     * 密码加密盐
     */
    private String salt;

    /**
     * 是否超级管理员
     */
    private Integer superAdmin;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 扩展预留信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Dict ext;
}
