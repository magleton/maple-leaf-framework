package com.geoxus.shiro.dto.req;

import com.geoxus.common.annotation.GXMergeSingleField;
import com.geoxus.common.annotation.GXValidateExtData;
import com.geoxus.common.dto.GXBaseDto;
import com.geoxus.shiro.constant.GXPermissionsConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXPermissionsReqDto extends GXBaseDto {
    /**
     * 核心模型ID
     */
    private static final int CORE_MODEL_ID = GXPermissionsConstant.CORE_MODEL_ID;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 权限码
     */
    private String code;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 扩展数据
     */
    @GXValidateExtData(tableName = GXPermissionsConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXMergeSingleField(fieldName = "author")
    private String author;
}
