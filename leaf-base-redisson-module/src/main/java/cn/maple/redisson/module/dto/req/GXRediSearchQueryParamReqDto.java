package cn.maple.redisson.module.dto.req;

import cn.maple.core.framework.dto.inner.GXBaseSEParamInnerDto;
import io.github.dengliming.redismodule.redisearch.search.Filter;
import io.github.dengliming.redismodule.redisearch.search.HighlightOptions;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Builder
public class GXRediSearchQueryParamReqDto extends GXBaseSEParamInnerDto {
    /**
     * 需要搜索的字段
     */
    private Set<String> inFields;

    /**
     * 高亮选项
     */
    private HighlightOptions highlightOptions;

    /**
     * 需要返回的字段
     */
    private Set<String> returnFields;

    /**
     * 过滤器
     */
    private List<Filter> filters;
}
