package com.geoxus.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -3393675841771619760L;

    private Long id;

    private String userName;

    private String passWord;
}
