package cn.maple.core.datasource.entity;

import cn.maple.core.framework.dto.GXBaseData;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class GXBaseEntity implements GXBaseData {
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
