package cn.maple.redisson.properties;

import lombok.Data;

@Data
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
