package cn.maple.core.datasource.model;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.model.GXBaseModel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXMyBatisModel extends GXBaseModel {
    @TableField(fill = FieldFill.INSERT)
    private Integer createdAt;

    @TableField(fill = FieldFill.UPDATE)
    private Integer updatedAt;

    @TableField(fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(fill = FieldFill.UPDATE)
    private String updatedBy;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Dict ext;

    @TableLogic(delval = "id")
    private Number is_deleted;
}
