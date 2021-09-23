package com.geoxus.order.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user")
@Document(collation = "t_user")
public class UserEntity extends GXBaseEntity {
    private static final long serialVersionUID = 3607593165684109949L;

    private Integer id;

    private String username;

    private String ext;
}
