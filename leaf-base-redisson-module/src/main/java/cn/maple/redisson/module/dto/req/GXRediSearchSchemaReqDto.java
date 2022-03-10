package cn.maple.redisson.module.dto.req;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Set;

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
    private String dataType;

    /**
     * 所依赖底层数据的前缀
     */
    private String prefix;

    /**
     * filter
     */
    private String filter;

    /**
     * 支持语言
     */
    private String language;

    /**
     * 指定需要language应用的字段
     */
    private Set<String> languageField;

    /**
     * 文档索引的score
     */
    private Float score;

    /**
     * 需要应用score的字段
     */
    private Set<String> scoreField;

    /**
     * 索引字段信息
     * eg:
     * Dict.create()
     * .set("$.name" , "name text")
     * .set("$.tags" , "tags Tag ';'");
     */
    private Dict schemas;
}
