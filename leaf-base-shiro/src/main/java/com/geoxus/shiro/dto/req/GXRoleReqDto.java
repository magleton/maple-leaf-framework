package com.geoxus.shiro.dto.req;

import com.geoxus.core.common.annotation.GXMergeSingleField;
import com.geoxus.core.common.annotation.GXValidateExtData;
import com.geoxus.common.dto.GXBaseDto;
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
    @GXValidateExtData(tableName = GXRoleConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXMergeSingleField(fieldName = "author")
    private String author;
}
