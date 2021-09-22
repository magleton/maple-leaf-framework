package com.geoxus.mongodb.properties;

import lombok.Data;

/**
 * 多数据源属性
 */
@Data
public class GXMongoDataSourceProperties {
    private String uri;

    private String replicaSet;

    private String username;

    private String password;

    private String database;

    private String authenticationDatabase;

    private Integer connectionsPerHost;

    private Integer minConnectionsPerHost;
}