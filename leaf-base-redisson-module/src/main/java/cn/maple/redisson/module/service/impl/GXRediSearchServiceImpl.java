package cn.maple.redisson.module.service.impl;

import cn.hutool.core.lang.Dict;
import cn.maple.redisson.module.dto.req.GXRediSearchQueryParamReqDto;
import cn.maple.redisson.module.dto.req.GXRediSearchSchemaReqDto;
import cn.maple.redisson.module.repository.GXRediSearchRepository;
import cn.maple.redisson.module.service.GXRediSearchService;
import io.github.dengliming.redismodule.redisearch.RediSearch;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GXRediSearchServiceImpl implements GXRediSearchService {
    @Resource
    private GXRediSearchRepository rediSearchRepository;

    /**
     * 创建索引结构
     *
     * @param schemaReqDto 索引结构描述对象
     * @return 索引创建是否成功
     */
    @Override
    public boolean createIndexSchema(GXRediSearchSchemaReqDto schemaReqDto) {
        return rediSearchRepository.createIndexSchema(schemaReqDto);
    }

    /**
     * 修改索引
     *
     * @param schemaReqDto 索引数据
     * @return 是否修改成功
     */
    @Override
    public boolean alertIndexSchema(GXRediSearchSchemaReqDto schemaReqDto) {
        return rediSearchRepository.alertIndexSchema(schemaReqDto);
    }

    /**
     * 修改索引
     *
     * @param schemaReqDto 索引数据
     * @param dropExists   是否删除已经存在的索引
     * @return 是否修改成功
     */
    @Override
    public boolean alertIndexSchema(GXRediSearchSchemaReqDto schemaReqDto, boolean dropExists) {
        if (dropExists) {
            dropIndexSchema(schemaReqDto);
        }
        return createIndexSchema(schemaReqDto);
    }

    /**
     * 删除索引
     *
     * @param schemaReqDto 索引结构请求数据
     * @return 是否成功
     */
    @Override
    public boolean dropIndexSchema(GXRediSearchSchemaReqDto schemaReqDto) {
        return rediSearchRepository.dropIndexSchema(schemaReqDto);
    }

    /**
     * 按照条件进行搜索
     *
     * @param paramInnerDto 搜索条件
     * @return List
     */
    @Override
    public List<Dict> search(GXRediSearchQueryParamReqDto paramInnerDto) {
        return rediSearchRepository.search(paramInnerDto, Dict.class);
    }

    /**
     * 获取RediSearch对象
     *
     * @param indexName 索引名字
     * @return RediSearch对象
     */
    @Override
    public RediSearch getRediSearch(String indexName) {
        return rediSearchRepository.getRediSearch(indexName);
    }
}
