package com.geoxus.shiro.dto.req;

import com.geoxus.core.common.annotation.GXMergeSingleFieldToJSONFieldAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.dto.GXBaseDto;
import com.geoxus.shiro.constant.GXRoleConstant;
import lombok.Data;

@Data
public class GXRoleReqDto extends GXBaseDto {
    /**
     * 核心模型ID
     */
    private static final int CORE_MODEL_ID = GXRoleConstant.CORE_MODEL_ID;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 角色名字
     */
    private String roleName;

    /**
     * 角色code
     */
    private String code;

    /**
     * 扩展数据
     */
    @GXValidateExtDataAnnotation(tableName = GXRoleConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXMergeSingleFieldToJSONFieldAnnotation
    private String author;
}
