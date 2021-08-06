package com.geoxus.commons.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("data_dict")
@EqualsAndHashCode(callSuper = false)
public class GXDataDictEntity extends GXBaseEntity {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Integer id;

    /**
     * 具体值
     */
    private String value;

    /**
     * 显示的label
     */
    private String label;

    /**
     * 所属类型
     */
    private String type;
}
