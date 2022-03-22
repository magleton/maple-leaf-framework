package cn.maple.redisson.module.service;

import cn.hutool.core.lang.Dict;
import cn.maple.redisson.module.dto.req.GXRediSearchQueryParamReqDto;
import cn.maple.redisson.module.dto.req.GXRediSearchSchemaReqDto;
import io.github.dengliming.redismodule.redisearch.RediSearch;

import java.util.List;

@SuppressWarnings("all")
public interface GXRediSearchService {
    /**
     * 创建索引结构
     *
     * @param schemaReqDto
     * @return
     */
    boolean createIndexSchema(GXRediSearchSchemaReqDto schemaReqDto);

    /**
     * 修改索引
     *
     * @param schemaReqDto 索引数据
     * @return 是否修改成功
     */
    boolean alertIndexSchema(GXRediSearchSchemaReqDto schemaReqDto);

    /**
     * 修改索引
     *
     * @param schemaReqDto 索引数据
     * @param dropExists   是否删除已经存在的索引
     * @return 是否修改成功
     */
    boolean alertIndexSchema(GXRediSearchSchemaReqDto schemaReqDto, boolean dropExists);

    /**
     * 删除索引
     *
     * @param schemaReqDto 索引结构请求数据
     * @return 是否成功
     */
    boolean deleteIndexSchema(GXRediSearchSchemaReqDto schemaReqDto);

    /**
     * 按照条件进行搜索
     *
     * @param paramInnerDto 搜索条件
     * @return List
     */
    List<Dict> search(GXRediSearchQueryParamReqDto searchQueryParamReqDto);

    /**
     * 获取RediSearch对象
     *
     * @param indexName 索引名字
     * @return RediSearch对象
     */
    RediSearch getRediSearch(String indexName);
}
