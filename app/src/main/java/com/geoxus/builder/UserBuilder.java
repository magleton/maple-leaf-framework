package com.geoxus.builder;

import org.apache.ibatis.jdbc.SQL;

public class UserBuilder {
    public String getUserInfo(Long userId) {
        return new SQL().FROM("user").SELECT("*").WHERE("id = " + userId).toString();
    }
}
