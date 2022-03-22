package cn.maple.redisson.module.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.redisson.module.dto.req.GXRediSearchQueryParamReqDto;
import cn.maple.redisson.module.dto.req.GXRediSearchSchemaReqDto;
import com.google.common.collect.Table;
import io.github.dengliming.redismodule.redisearch.RediSearch;
import io.github.dengliming.redismodule.redisearch.client.RediSearchClient;
import io.github.dengliming.redismodule.redisearch.index.Document;
import io.github.dengliming.redismodule.redisearch.index.IndexDefinition;
import io.github.dengliming.redismodule.redisearch.index.IndexOptions;
import io.github.dengliming.redismodule.redisearch.index.RSLanguage;
import io.github.dengliming.redismodule.redisearch.index.schema.*;
import io.github.dengliming.redismodule.redisearch.search.SearchOptions;
import io.github.dengliming.redismodule.redisearch.search.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Repository
public class GXRediSearchRepository {
    @Resource
    private RediSearchClient rediSearchClient;

    /**
     * 通过条件获取数据
     *
     * @param dataIndexesParamInnerDto 索引数据
     * @param targetClass              目标类型
     * @param customerData             额外数据
     * @return 列表
     */
    public <R> List<R> search(GXRediSearchQueryParamReqDto dataIndexesParamInnerDto, Class<R> targetClass, Object... customerData) {
        String indexName = dataIndexesParamInnerDto.getIndexName();
        RediSearch rediSearch = getRediSearch(indexName);
        SearchOptions searchOptions = Optional.ofNullable(dataIndexesParamInnerDto.getSearchOptions()).orElse(new SearchOptions());
        String query = dataIndexesParamInnerDto.getQuery();

        if (CharSequenceUtil.isBlank(query)) {
            return Collections.emptyList();
        }

        SearchResult search = rediSearch.search(query, searchOptions);
        List<Document> documents = search.getDocuments();
        List<R> rLst = new ArrayList<>();
        documents.forEach(doc -> {
            Dict data = Dict.create();
            data.put("id", doc.getId());
            doc.getFields().forEach((k, v) -> {
                if (CharSequenceUtil.equals("$", k) && JSONUtil.isTypeJSON(v.toString())) {
                    data.putAll(JSONUtil.toBean(v.toString(), Dict.class));
                }
            });
            R r = GXCommonUtils.convertSourceToTarget(data, targetClass, dataIndexesParamInnerDto.getConvertMethodName(), dataIndexesParamInnerDto.getCopyOptions(), customerData);
            rLst.add(r);
        });
        return rLst;
    }

    /**
     * 创建或者修改索引结构
     *
     * @param schemaReqDto 索引数据
     * @return 新增是否成功
     */
    @SuppressWarnings("all")
    public boolean createIndexSchema(GXRediSearchSchemaReqDto schemaReqDto) {
        String indexName = schemaReqDto.getIndexName();
        Table<String, String, FieldType> schemaFields = schemaReqDto.getSchemaFields();
        if (Objects.isNull(schemaFields) || schemaFields.isEmpty()) {
            log.info(CharSequenceUtil.format("修改索引失败"));
            return false;
        }
        String separator = Optional.ofNullable(schemaReqDto.getSeparator()).orElse(",");
        RSLanguage language = Optional.ofNullable(schemaReqDto.getLanguage()).orElse(RSLanguage.CHINESE);
        IndexDefinition.DataType dataType = Optional.ofNullable(schemaReqDto.getDataType()).orElse(IndexDefinition.DataType.JSON);
        Map<String, Map<String, FieldType>> rowMap = schemaFields.rowMap();
        Schema schema = new Schema();
        rowMap.forEach((identifier, fields) -> {
            fields.forEach((attribute, fieldType) -> {
                schema.addField(fittingField(fieldType, identifier, attribute, separator));
            });
        });
        List<String> prefixes = schemaReqDto.getPrefixes();
        IndexDefinition indexDefinition = new IndexDefinition(dataType);
        indexDefinition.setPrefixes(prefixes);
        indexDefinition.setLanguage(language);
        IndexOptions indexOptions = new IndexOptions();
        indexOptions.definition(indexDefinition);
        return getRediSearch(indexName).createIndex(schema, indexOptions);
    }

    /**
     * 修改索引结构
     *
     * @param schemaReqDto 索引信息
     * @return 修改是是否成功
     */
    @SuppressWarnings("all")
    public boolean alertIndexSchema(GXRediSearchSchemaReqDto schemaReqDto) {
        String indexName = schemaReqDto.getIndexName();
        Table<String, String, FieldType> schemaFields = schemaReqDto.getSchemaFields();
        if (Objects.isNull(schemaFields) || schemaFields.isEmpty()) {
            log.info(CharSequenceUtil.format("修改索引失败"));
            return false;
        }
        Map<String, Map<String, FieldType>> rowMap = schemaFields.rowMap();
        List<Field> fieldLst = CollUtil.newArrayList();
        rowMap.forEach((identifier, fields) -> {
            fields.forEach((attribute, fieldType) -> {
                fieldLst.add(fittingField(fieldType, identifier, attribute, schemaReqDto.getSeparator()));
            });
        });
        return getRediSearch(indexName).alterIndex(fieldLst.toArray(new Field[0]));
    }

    /**
     * 删除索引
     *
     * @param schemaReqDto 索引数据
     * @return 删除索引是否成功
     */
    @SuppressWarnings("all")
    public boolean dropIndexSchema(GXRediSearchSchemaReqDto schemaReqDto) {
        String indexName = schemaReqDto.getIndexName();
        return getRediSearch(indexName).dropIndex(true);
    }

    /**
     * 获取RediSearch对象
     *
     * @param indexName 索引名字
     * @return RediSearch对象
     */
    public RediSearch getRediSearch(String indexName) {
        return rediSearchClient.getRediSearch(indexName);
    }

    /**
     * 组装索引Field
     *
     * @param fieldType 字段类型
     * @return Field
     */
    private Field fittingField(FieldType fieldType, String identifier, String attribute, String separator) {
        if (fieldType.equals(FieldType.TEXT)) {
            Field field = new TextField(identifier);
            field.attribute(attribute);
            return field;
        } else if (fieldType.equals(FieldType.TAG)) {
            separator = Objects.isNull(separator) ? "," : separator;
            TagField field = new TagField(identifier, separator);
            field.attribute(attribute);
            return field;
        }
        Field field = new Field(identifier, fieldType);
        field.attribute(attribute);
        return field;
    }
}
