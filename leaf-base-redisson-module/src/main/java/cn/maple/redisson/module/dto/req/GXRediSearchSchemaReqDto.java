package cn.maple.redisson.module.dto.req;

import cn.maple.core.framework.dto.req.GXBaseReqDto;
import com.google.common.collect.Table;
import io.github.dengliming.redismodule.redisearch.index.IndexDefinition;
import io.github.dengliming.redismodule.redisearch.index.RSLanguage;
import io.github.dengliming.redismodule.redisearch.index.schema.FieldType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Builder
public class GXRediSearchSchemaReqDto extends GXBaseReqDto {
    /**
     * 索引名字
     */
    private String indexName;

    /**
     * 索引所依赖的底层数据类型
     * 1、HASH
     * 2、JSON
     */
    private IndexDefinition.DataType dataType;

    /**
     * 索引结构的字段信息
     */
    private transient Table<String, String, FieldType> schemaFields;

    /**
     * 索引前缀
     */
    private List<String> prefixes;

    /**
     * 索引指定的语言
     */
    private RSLanguage language;

    /**
     * TAG字段的分割符
     */
    private String separator = ",";
}
