package com.geoxus.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.geoxus.core.framework.dto.GXBaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseEntity extends GXBaseData {
    private static final long serialVersionUID = 2923482261586642602L;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(fill = FieldFill.UPDATE)
    private Long updatedAt;

    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
    
    @TableField(fill = FieldFill.UPDATE)
    private String updatedBy;
}
