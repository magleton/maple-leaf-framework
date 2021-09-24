package com.geoxus.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.common.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user")
@Document(collection = "t_user")
public class UserDto extends GXBaseDto {
    private static final long serialVersionUID = 3607593165684109949L;

    private String id;

    private String username;

    private String ext;

    private Integer age;
}
