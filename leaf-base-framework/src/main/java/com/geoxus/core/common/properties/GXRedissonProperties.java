package com.geoxus.core.common.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;

@Data
@ConditionalOnMissingClass(value = {"com.alibaba.nacos.api.config.ConfigFactory"})
public class GXRedissonProperties {
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
