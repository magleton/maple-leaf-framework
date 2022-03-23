package cn.maple.redisson.module.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.redisson.module.dto.req.GXRediSearchQueryParamReqDto;
import cn.maple.redisson.module.dto.req.GXRediSearchSchemaReqDto;
import cn.maple.redisson.module.repository.GXRediSearchRepository;
import cn.maple.redisson.module.service.GXRediSearchService;
import io.github.dengliming.redismodule.redisearch.RediSearch;
import io.github.dengliming.redismodule.redisearch.index.IndexDefinition;
import io.github.dengliming.redismodule.redisearch.index.schema.Field;
import io.github.dengliming.redismodule.redisearch.index.schema.FieldType;
import io.github.dengliming.redismodule.redisearch.index.schema.TagField;
import io.github.dengliming.redismodule.redisearch.index.schema.TextField;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public boolean updateOrCreateIndexSchema(GXRediSearchSchemaReqDto schemaReqDto) {
        return rediSearchRepository.createIndexSchema(schemaReqDto);
    }

    /**
     * 创建或者更新索引
     *
     * @param fieldLst       字段信息
     * @param indexName      索引名字
     * @param prefixes       索引前缀
     * @param schemaDataType 索引依赖的数据类型  HASH JSON
     * @return boolean
     */
    @Override
    public boolean updateOrCreateIndexSchema(List<Dict> fieldLst, String indexName, List<String> prefixes, IndexDefinition.DataType schemaDataType) {
        List<Field> schemaFieldLst = processIndexSchemaFieldLst(fieldLst, schemaDataType);
        GXRediSearchSchemaReqDto schemaReqDto = GXRediSearchSchemaReqDto.builder().schemaFieldLst(schemaFieldLst).indexName(indexName).prefixes(prefixes).build();
        dropIndexSchema(schemaReqDto);
        return updateOrCreateIndexSchema(schemaReqDto);
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
        return updateOrCreateIndexSchema(schemaReqDto);
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

    /**
     * 处理索引字段
     *
     * @param fieldLst       字段信息
     * @param schemaDataType 索引结构类型
     * @return 处理之后的索引数据字段
     */
    private List<Field> processIndexSchemaFieldLst(List<Dict> fieldLst, IndexDefinition.DataType schemaDataType) {
        List<Field> retFieldLst = new ArrayList<>();
        fieldLst.forEach(field -> retFieldLst.add(processField(field, schemaDataType)));
        return retFieldLst;
    }

    /**
     * 将指定的field配置组合成索引的字段结构
     *
     * @param fieldData      字段数据
     * @param schemaDataType 索引结构源数据类型  HASH  JSON
     * @return 索引字段结构
     */
    private Field processField(Dict fieldData, IndexDefinition.DataType schemaDataType) {
        String dataType = fieldData.getStr("dataType");
        String code = fieldData.getStr("code");
        String template = "{}";
        if (CharSequenceUtil.equalsIgnoreCase(schemaDataType.name(), IndexDefinition.DataType.JSON.name())) {
            template = "$.{}";
        }
        String identifier = CharSequenceUtil.format(template, code);
        if (CharSequenceUtil.equalsIgnoreCase(dataType, FieldType.TEXT.name())) {
            return new TextField(identifier).attribute(code);
        } else if (CharSequenceUtil.equalsIgnoreCase(dataType, FieldType.TAG.name())) {
            return new TagField(identifier, ",").attribute(code);
        }
        return new Field(identifier, FieldType.valueOf(dataType)).attribute(code);
    }
}
