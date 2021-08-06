package com.geoxus.shiro.dto.req;

import com.geoxus.core.common.annotation.GXMergeSingleFieldToJSONFieldAnnotation;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.dto.GXBaseDto;
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
     * 扩展数据
     */
    @GXValidateExtDataAnnotation(tableName = GXPermissionsConstant.TABLE_NAME)
    private String ext;

    /**
     * 作者
     */
    @GXMergeSingleFieldToJSONFieldAnnotation
    private String author;
}
