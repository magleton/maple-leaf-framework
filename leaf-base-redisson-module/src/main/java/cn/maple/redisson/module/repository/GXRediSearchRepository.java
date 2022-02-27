package cn.maple.redisson.module.repository;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.ddd.repository.GXBaseSERepository;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.redisson.module.dto.GXBaseRediSeachParamInnerDto;
import io.github.dengliming.redismodule.redisearch.RediSearch;
import io.github.dengliming.redismodule.redisearch.client.RediSearchClient;
import io.github.dengliming.redismodule.redisearch.index.Document;
import io.github.dengliming.redismodule.redisearch.index.RSLanguage;
import io.github.dengliming.redismodule.redisearch.search.Filter;
import io.github.dengliming.redismodule.redisearch.search.HighlightOptions;
import io.github.dengliming.redismodule.redisearch.search.SearchOptions;
import io.github.dengliming.redismodule.redisearch.search.SearchResult;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public class GXRediSearchRepository implements GXBaseSERepository<GXBaseRediSeachParamInnerDto> {
    @Resource
    private RediSearchClient rediSearchClient;

    @Override
    public <R> List<R> search(GXBaseRediSeachParamInnerDto rsDataIndexesParamInnerDto, Class<R> targetClass) {
        String indexName = rsDataIndexesParamInnerDto.getIndexName();
        RediSearch rediSearch = rediSearchClient.getRediSearch(indexName);
        String queryWords = rsDataIndexesParamInnerDto.getQueryWords();
        if (CharSequenceUtil.isBlank(queryWords)) {
            return Collections.emptyList();
        }
        SearchOptions searchOptions = new SearchOptions();
        searchOptions.language(RSLanguage.CHINESE);
        Set<String> inFields = rsDataIndexesParamInnerDto.getInFields();
        HighlightOptions highlightOptions = rsDataIndexesParamInnerDto.getHighlightOptions();
        Set<String> returnFields = rsDataIndexesParamInnerDto.getReturnFields();
        List<Filter> filters = rsDataIndexesParamInnerDto.getFilters();
        if (Objects.nonNull(inFields)) {
            searchOptions.inFields(inFields.toArray(new String[0]));
        }
        if (Objects.nonNull(highlightOptions)) {
            searchOptions.highlightOptions(highlightOptions);
        }
        if (Objects.nonNull(returnFields)) {
            searchOptions.returnFields(returnFields.toArray(new String[0]));
        }
        if (Objects.nonNull(filters) && !filters.isEmpty()) {
            filters.forEach(searchOptions::filter);
        }
        SearchResult search = rediSearch.search(queryWords, searchOptions);
        List<Document> documents = search.getDocuments();
        return GXCommonUtils.convertSourceListToTargetList(documents, targetClass, rsDataIndexesParamInnerDto.getConvertMethodName(), rsDataIndexesParamInnerDto.getCopyOptions());
    }
}
