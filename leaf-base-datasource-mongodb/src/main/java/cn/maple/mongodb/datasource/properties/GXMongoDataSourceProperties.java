package cn.maple.mongodb.datasource.properties;

import lombok.Data;
import org.bson.UuidRepresentation;

/**
 * 多数据源属性
 */
@Data
@SuppressWarnings("all")
public class GXMongoDataSourceProperties {
    /**
     * Mongo server port. Cannot be set with URI.
     */
    private final Integer port = null;

    /**
     * Representation to use when converting a UUID to a BSON binary value.
     */
    private final UuidRepresentation uuidRepresentation = UuidRepresentation.JAVA_LEGACY;

    /**
     * Mongo server host. Cannot be set with URI.
     */
    private String host;

    /**
     * Mongo database URI. Cannot be set with host, port, credentials and replica set
     * name.
     */
    private String uri;

    /**
     * Database name.
     */
    private String database;

    /**
     * Authentication database name.
     */
    private String authenticationDatabase;

    /**
     * GridFS database name.
     */
    private String gridFsDatabase;

    /**
     * Login user of the mongo server. Cannot be set with URI.
     */
    private String username;

    /**
     * Login password of the mongo server. Cannot be set with URI.
     */
    private char[] password;

    /**
     * Required replica set name for the cluster. Cannot be set with URI.
     */
    private String replicaSetName;

    /**
     * Fully qualified name of the FieldNamingStrategy to use.
     */
    private Class<?> fieldNamingStrategy;

    private String beanName;

    private Boolean isPrimary = false;
}
