package com.geoxus.mongodb.factory;

import com.geoxus.common.util.GXSpringContextUtil;
import com.geoxus.mongodb.config.GXMongoDynamicContextHolder;
import com.geoxus.mongodb.properties.GXMongoDataSourceProperties;
import com.geoxus.mongodb.properties.GXMongoDynamicDataSourceProperties;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Optional;

public class GXSimpleMongoClientDatabaseFactory extends SimpleMongoClientDatabaseFactory {
    public GXSimpleMongoClientDatabaseFactory(String connectionString) {
        super(connectionString);
    }

    public GXSimpleMongoClientDatabaseFactory(ConnectionString connectionString) {
        super(connectionString);
    }

    public GXSimpleMongoClientDatabaseFactory(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName);
    }

    @Override
    protected MongoDatabase doGetMongoDatabase(String dbName) {
        String peek = Optional.ofNullable(GXMongoDynamicContextHolder.peek()).orElse("framework");
        GXMongoDynamicDataSourceProperties dataSourceProperties = GXSpringContextUtil.getBean(GXMongoDynamicDataSourceProperties.class);
        assert dataSourceProperties != null;
        GXMongoDataSourceProperties mongoDataSourceProperties = dataSourceProperties.getDatasource().get(peek);
        String currentDBName = mongoDataSourceProperties.getDatabase();
        return super.doGetMongoDatabase(currentDBName);
    }
}
