package cn.maple.redisson.module.dto.req;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.maple.core.framework.constant.GXCommonConstant;
import io.github.dengliming.redismodule.redisearch.search.SearchOptions;
import lombok.Builder;
import lombok.Getter;

/**
 * RediSearch 查询条件
 *
 * @author britton
 * @see <a href="https://oss.redis.com/redisearch/Commands/#ftsearch">...</a>
 */
@Getter
@Builder
public class GXRediSearchQueryParamReqDto {
    /**
     * 索引名字
     */
    private String indexName;

    /**
     * 查询语句
     * 参考 FT.SEARCH {indexName} {query}
     */
    private String query;

    /**
     * 搜索选项
     */
    private SearchOptions searchOptions;

    /**
     * 对象复制时的一些额外配置
     */
    private CopyOptions copyOptions;

    /**
     * 数据转换时需要条用的额外参数
     */
    private String convertMethodName;

    /**
     * 当前页
     */
    private Integer currentPage = GXCommonConstant.DEFAULT_CURRENT_PAGE;

    /**
     * 每页条数
     */
    private Integer pageSize = GXCommonConstant.DEFAULT_PAGE_SIZE;
}
