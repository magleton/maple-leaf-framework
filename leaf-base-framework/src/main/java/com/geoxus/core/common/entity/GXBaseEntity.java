package com.geoxus.core.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class GXBaseEntity implements Serializable {
    @TableField(fill = FieldFill.INSERT)
    private Integer createdAt;

    @TableField(fill = FieldFill.UPDATE)
    private Integer updatedAt;
}
