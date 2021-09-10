package com.geoxus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;

@Data
@TableName("user")
public class UserEntity extends GXBaseEntity {
    private Integer id;

    private String username;

    private String ext;
}
