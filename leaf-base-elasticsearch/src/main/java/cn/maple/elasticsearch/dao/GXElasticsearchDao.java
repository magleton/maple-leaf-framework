package cn.maple.elasticsearch.dao;

import cn.maple.elasticsearch.model.GXElasticsearchModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.io.Serializable;

public interface GXElasticsearchDao<T extends GXElasticsearchModel, ID extends Serializable> extends ElasticsearchRepository<T, ID> {
}