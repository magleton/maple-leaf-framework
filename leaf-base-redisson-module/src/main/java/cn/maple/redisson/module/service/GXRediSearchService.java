package cn.maple.redisson.module.service;

import cn.hutool.core.lang.Dict;
import cn.maple.redisson.module.dto.req.GXRediSearchQueryParamReqDto;
import cn.maple.redisson.module.dto.req.GXRediSearchSchemaReqDto;
import io.github.dengliming.redismodule.redisearch.RediSearch;
import io.github.dengliming.redismodule.redisearch.index.IndexDefinition;

import java.util.List;

@SuppressWarnings("all")
public interface GXRediSearchService {
    /**
     * 创建索引结构
     *
     * @param schemaReqDto
     * @return
     */
    boolean updateOrCreateIndexSchema(GXRediSearchSchemaReqDto schemaReqDto);

    /**
     * 创建或者更新索引
     *
     * @param fieldLst       字段信息
     * @param indexName      索引名字
     * @param prefixes       索引前缀
     * @param schemaDataType 索引依赖的数据类型  HASH JSON
     * @return boolean
     */
    boolean updateOrCreateIndexSchema(List<Dict> fieldLst, String indexName, List<String> prefixes, IndexDefinition.DataType schemaDataType);

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
    boolean dropIndexSchema(GXRediSearchSchemaReqDto schemaReqDto);

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
