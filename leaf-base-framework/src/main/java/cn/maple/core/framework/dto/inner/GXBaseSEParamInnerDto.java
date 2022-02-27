package cn.maple.core.framework.dto.inner;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.GXBaseDto;
import com.google.common.collect.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * 外置搜索引擎的参数配置DTO
 * SE: Search Engine
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GXBaseSEParamInnerDto extends GXBaseDto {
    /**
     * 索引名字
     */
    private String indexName;

    /**
     * 搜索条件
     */
    private transient Table<String, String, Object> condition;

    /**
     * 当前页(偏移页)
     */
    private Integer page;

    /**
     * 每页数据条数
     */
    private Integer pageSize;

    /**
     * 需要查询的数据列
     */
    private Set<String> columns;

    /**
     * 排序字段
     * eg:
     * Dict.create().set("created_at" , "desc").set("username" , "asc");
     */
    private Dict sortByField;

    /**
     * 分组字段
     */
    private Set<String> groupByField;

    /**
     * 归并条件
     * eg:
     * REDUCE  COUNT 0
     */
    private String reduce;

    /**
     * 对象复制时的一些额外配置
     */
    private CopyOptions copyOptions;

    /**
     * 应用的函数
     * eg:
     * APPLY "contains(@name,\"bri\")" as cnt
     */
    private String apply;

    /**
     * 索引中的标签
     */
    private Set<String> tags;

    /**
     * tag字段的分隔符
     * eg:
     * foo;bar
     * foo:bar
     */
    private String tagSeparator;

    /**
     * 过滤条件
     * eg:
     * filter "@cnt > 0"
     */
    private String filter;

    /**
     * 额外参数
     * 用于一些特殊场景
     * eg:
     * 1、不合适放入condition中的参数
     * 2、需要使用该参数调整查询的规则
     */
    private transient Dict params;
}
