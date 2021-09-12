package com.geoxus.shiro.dto.req;

import com.geoxus.core.common.annotation.GXSingleFieldToDbJsonFieldAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.dto.GXBaseDto;
import com.geoxus.shiro.constant.GXAdminConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXAdminReqDto extends GXBaseDto {
    /**
     * 核心模型ID
     */
    private static final int CORE_MODEL_ID = GXAdminConstant.CORE_MODEL_ID;

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
     * 账号状态
     */
    private Integer status;

    /**
     * 是否超级管理员 0 否 1 是
     */
    private Integer superAdmin = 0;

    /**
     * 扩展数据
     */
    @GXValidateExtDataAnnotation(tableName = GXAdminConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXSingleFieldToDbJsonFieldAnnotation(fieldName = "author")
    private String author;
}
