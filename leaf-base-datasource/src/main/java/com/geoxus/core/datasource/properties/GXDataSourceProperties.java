package com.geoxus.core.datasource.properties;

import lombok.Data;

/**
 * 多数据源属性
 */
@Data
public class GXDataSourceProperties {
    /**
     * 默认的数据库驱动
     */
    private String driverClassName = "com.p6spy.engine.spy.P6SpyDriver";

    /**
     * 连接的URL
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;
    /**
     * Druid默认参数
     */
    private int initialSize = 2;

    private int maxActive = 10;

    private int minIdle = -1;

    private long maxWait = 60 * 1000L;

    private long timeBetweenEvictionRunsMillis = 60 * 1000L;

    private long minEvictableIdleTimeMillis = 1000L * 60L * 30L;

    private long maxEvictableIdleTimeMillis = 1000L * 60L * 60L * 7;

    private String validationQuery = "select 1";

    private int validationQueryTimeout = -1;

    private boolean testOnBorrow = false;

    private boolean testOnReturn = false;

    private boolean testWhileIdle = true;

    private boolean poolPreparedStatements = false;

    private int maxOpenPreparedStatements = -1;

    private boolean sharePreparedStatements = false;

    private String filters = "stat,wall";

    private String dbType = "mysql";
}