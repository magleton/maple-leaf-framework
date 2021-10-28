package com.geoxus.redisson.properties;

import lombok.Data;

@Data
//@ConditionalOnMissingClass(value = {"com.alibaba.nacos.api.config.ConfigFactory"})
public class GXRedissonConnectProperties {
    /**
     * 链接地址
     */
    private String address;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库
     */
    private Integer database;
}
