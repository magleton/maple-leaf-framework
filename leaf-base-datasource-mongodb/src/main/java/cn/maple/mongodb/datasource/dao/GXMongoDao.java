package cn.maple.mongodb.datasource.dao;

import cn.maple.mongodb.datasource.model.GXMongoModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;

public interface GXMongoDao<T extends GXMongoModel, ID extends Serializable> extends MongoRepository<T, ID> {
}
