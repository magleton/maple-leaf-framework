package com.geoxus.shiro.dto.req;

import com.geoxus.core.framework.annotation.GXMergeSingleField;
import com.geoxus.core.framework.annotation.GXValidateExtData;
import com.geoxus.core.framework.dto.GXBaseDto;
import com.geoxus.shiro.constant.GXPermissionsConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXPermissionsReqDto extends GXBaseDto {
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
