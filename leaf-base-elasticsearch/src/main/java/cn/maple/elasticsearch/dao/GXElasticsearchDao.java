package cn.maple.elasticsearch.dao;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXSpringContextUtils;
import cn.maple.elasticsearch.constant.GXEsCriteriaMethodMappingConstant;
import cn.maple.elasticsearch.model.GXElasticsearchModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于ES Repository封装的统一基本操作
 * <p>
 * {@see <a href="https://docs.spring.io/spring-data/elasticsearch/reference/index.html">帮助文档</a>}
 * <p>
 * {@see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/7.17/query-dsl.html">ES查询语法</a>}
 *
 * @param <T>  实体的类型
 * @param <ID> 实体中的ID序列号
 */
public interface GXElasticsearchDao<T extends GXElasticsearchModel, Q extends BaseQuery, B extends BaseQueryBuilder<Q, B>, ID extends Serializable> extends ElasticsearchRepository<T, ID> {
    /**
     * 根据条件查询所有满足条件的数据
     *
     * @param queryParamInnerDto 查询条件对象
     * @return 返回查询到数据列表
     */
    default List<Dict> findByCondition(GXBaseQueryParamInnerDto queryParamInnerDto) {
        Dict queryData = executeQuery(queryParamInnerDto);
        List<Dict> lst = Convert.convert(new TypeReference<>() {
        }, queryData.getObj("records"));
        return lst.stream().map(d -> Convert.convert(Dict.class, d.getObj("content"))).collect(Collectors.toList());
    }

    /**
     * 根据指定条件查询一条数据
     *
     * @param queryParamInnerDto 查询条件
     * @return 满足条件的一条数据
     */
    default Dict findOneByCondition(GXBaseQueryParamInnerDto queryParamInnerDto) {
        queryParamInnerDto.setPage(0);
        queryParamInnerDto.setPageSize(1);
        Dict queryData = executeQuery(queryParamInnerDto);
        List<Dict> lst = Convert.convert(new TypeReference<>() {
        }, queryData.getObj("records"));
        Optional<Dict> data = lst.stream().findFirst();
        return data.map(t -> Convert.convert(Dict.class, t.getObj("content"))).orElse(null);
    }

    /**
     * 分页查询
     *
     * @param queryParamInnerDto 查询条件
     * @return 分页数据
     */
    default GXPaginationResDto<Dict> paginate(GXBaseQueryParamInnerDto queryParamInnerDto) {
        Dict queryData = executeQuery(queryParamInnerDto);
        List<Dict> lst = Convert.convert(new TypeReference<>() {
        }, queryData.getObj("records"));
        List<Dict> records = lst.stream().map(d -> {
            Dict content = Convert.convert(Dict.class, d.getObj("content"));
            Dict highlightFields = Convert.convert(Dict.class, d.getObj("highlightFields"));
            content.putAll(highlightFields);
            return content;
        }).collect(Collectors.toList());
        long totalCount = queryData.getInt("totalHits");
        long currentPage = queryParamInnerDto.getPage();
        long pageSize = queryParamInnerDto.getPageSize();
        long pages = totalCount / pageSize;
        return new GXPaginationResDto<>(records, totalCount, pages, pageSize, currentPage);
    }

    /**
     * 根据条件创建数据
     * 如果数据存在 则先将数据删除
     * 然后再新增
     *
     * @param entity    需要新增的数据
     * @param condition 需要被删除数据的查询条件
     * @return ID
     */
    default <ID extends Serializable> ID updateOrCreate(T entity, List<GXCondition<?>> condition) {
        T save = save(entity);
        Class<ID> retIDClazz = GXCommonUtils.getGenericClassType((Class<?>) getClass().getGenericInterfaces()[0], 3);
        String methodName = CharSequenceUtil.format("get{}", CharSequenceUtil.upperFirst("id"));
        return Convert.convert(retIDClazz, GXCommonUtils.reflectCallObjectMethod(save, methodName));
    }

    /**
     * 删除满足条件的数据
     *
     * @param tableName 表的名字
     * @param condition 删除条件
     * @return 被成功删除的数量
     */
    default Integer deleteCondition(String tableName, List<GXCondition<?>> condition) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().condition(condition).build();
        Query query = buildQuery(queryParamInnerDto);
        ElasticsearchTemplate elasticsearchTemplate = getElasticsearchTemplate();
        ByQueryResponse deleteResponse = elasticsearchTemplate.delete(query, getGenericClassType());
        return Math.toIntExact(deleteResponse.getDeleted());
    }

    /**
     * 执行统一查询
     *
     * @param queryParamInnerDto 查询条件
     * @return 查询到的数据
     */
    default Dict executeQuery(GXBaseQueryParamInnerDto queryParamInnerDto) {
        Q query = buildQuery(queryParamInnerDto);
        query = buildOrderBy(query, queryParamInnerDto);
        query = buildPageable(query, queryParamInnerDto);
        ElasticsearchTemplate elasticsearchTemplate = getElasticsearchTemplate();
        Class<?> genericClassType = GXCommonUtils.getGenericClassType((Class<?>) getClass().getGenericInterfaces()[0], 0);
        String indexName = queryParamInnerDto.getTableName();
        SearchHits<?> search = CharSequenceUtil.isEmpty(indexName) ? elasticsearchTemplate.search(query, genericClassType) : elasticsearchTemplate.search(query, genericClassType, IndexCoordinates.of(indexName));
        String[] methodName = new String[]{queryParamInnerDto.getMethodName()};
        if (CharSequenceUtil.isEmpty(methodName[0])) {
            methodName[0] = GXCommonConstant.DEFAULT_CUSTOMER_PROCESS_METHOD_NAME;
        }
        CopyOptions copyOptions = ObjectUtil.defaultIfNull(queryParamInnerDto.getCopyOptions(), GXCommonUtils::getDefaultCopyOptions);
        Function<SearchHit<?>, Dict> rowMapper = obj -> {
            Object extraData = Optional.ofNullable(queryParamInnerDto.getExtraData()).orElse(Dict.create());
            return GXCommonUtils.convertSourceToTarget(obj, Dict.class, methodName[0], copyOptions, extraData);
        };
        List<Dict> records = search.getSearchHits().stream().map(rowMapper).collect(Collectors.toList());
        long totalHits = search.getTotalHits();
        return Dict.create().set("totalHits", totalHits).set("records", records);
    }

    /**
     * 构建查询条件
     *
     * @param queryParamInnerDto 查询条件对象
     * @return ES的查询条件
     */
    @SuppressWarnings("all")
    default Q buildQuery(GXBaseQueryParamInnerDto queryParamInnerDto) {
        Class<BaseQuery> queryClazz = GXCommonUtils.getGenericClassType((Class<?>) getClass().getGenericInterfaces()[0], 1);
        B queryBuilder = buildQueryBuilder(queryParamInnerDto);

        BaseQuery query = ReflectUtil.newInstance(queryClazz, queryBuilder);
        return (Q) query;
    }

    /**
     * 构造查询Builder
     *
     * <pre>{@code
     * // 一个输入查询多字段
     * //nativeSearchQueryBuilder.withQuery(multiMatchQueryBuilder);
     * NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder();
     * nativeQueryBuilder.withQuery(
     *         new Query.Builder()
     *                 .multiMatch(new MultiMatchQuery.Builder().fields("name", "summary")
     *                         .query("陈子曦/陈茗泽""要问测试")
     *                         .operator(Operator.And)
     *                         .build())
     *                 .build());
     * // 指定字段查询
     * Query nameMatchQuery = new MatchQuery.Builder().field("name").query("陈子曦/陈茗泽").operator(Operator.Or).build()._toQuery();
     * Query summaryMatchQuery = new MatchQuery.Builder().field("summary").query("这个信息很重要").operator(Operator.Or).build()._toQuery();
     * BoolQuery boolQuery = new BoolQuery.Builder().must(CollUtil.newArrayList(nameMatchQuery, summaryMatchQuery)).build();
     * nativeQueryBuilder.withQuery(new Query.Builder().bool(boolQuery).build());
     * return nativeQueryBuilder;
     * }</pre>
     *
     * @param queryParamInnerDto 查询条件
     * @return 查询Builder
     */
    @SuppressWarnings("all")
    default B buildQueryBuilder(GXBaseQueryParamInnerDto queryParamInnerDto) {
        Class<BaseQueryBuilder<Q, B>> queryBuilderClazz = GXCommonUtils.getGenericClassType((Class<?>) getClass().getGenericInterfaces()[0], 2);

        if (queryBuilderClazz.isAssignableFrom(CriteriaQueryBuilder.class)) {
            Criteria criteria = conditions2Criteria(queryParamInnerDto);
            BaseQueryBuilder<Q, B> queryBuilder = ReflectUtil.newInstance(queryBuilderClazz, criteria);
            return (B) queryBuilder;
        }
        BaseQueryBuilder<Q, B> queryBuilder = ReflectUtil.newInstance(queryBuilderClazz);
        return (B) queryBuilder;
    }

    /**
     * 构建分页信息
     *
     * @param query              查询对象
     * @param queryParamInnerDto 查询条件
     * @return 查询对象
     */
    default Q buildPageable(Q query, GXBaseQueryParamInnerDto queryParamInnerDto) {
        // 处理分页
        int page = NumberUtil.max(Optional.ofNullable(queryParamInnerDto.getPage()).orElse(0) - 1, 0);
        int pageSize = Optional.ofNullable(queryParamInnerDto.getPageSize()).orElse(GXCommonConstant.DEFAULT_MAX_PAGE_SIZE);
        query.setPageable(PageRequest.of(page, pageSize));
        return query;
    }

    /**
     * 构建排序字段信息
     *
     * @param query              查询对象
     * @param queryParamInnerDto 查询条件
     * @return 查询对象
     */
    default Q buildOrderBy(Q query, GXBaseQueryParamInnerDto queryParamInnerDto) {
        Map<String, String> orderByField = queryParamInnerDto.getOrderByField();
        // 处理字段排序
        if (!CollUtil.isEmpty(orderByField)) {
            List<Sort.Order> orders = CollUtil.newArrayList();
            orderByField.keySet().forEach(column -> {
                String value = orderByField.get(column);
                if (Sort.Direction.DESC.equals(Sort.Direction.fromString(value))) {
                    orders.add(Sort.Order.desc(column));
                } else if (Sort.Direction.ASC.equals(Sort.Direction.fromString(value))) {
                    orders.add(Sort.Order.asc(column));
                }
            });
            Sort sort = Sort.by(orders);
            query.addSort(sort);
        }
        return query;
    }

    /**
     * 业务逻辑需要自定义查询条件
     * 留着扩展使用
     *
     * @param criteria 查询条件对象
     * @return criteria
     */
    default Criteria buildCriteria(Criteria criteria) {
        return criteria;
    }

    /**
     * 转换GXCondition列表为Criteria
     *
     * @param dbQueryParamInnerDto 查询信息
     * @return Criteria
     */
    default Criteria conditions2Criteria(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        List<GXCondition<?>> conditions = dbQueryParamInnerDto.getCondition();
        Criteria criteria = new Criteria();
        Map<String, String> methodMapping = GXEsCriteriaMethodMappingConstant.METHOD_MAPPING;
        if (CollUtil.isNotEmpty(conditions)) {
            conditions.forEach(condition -> {
                String fieldName = condition.getFieldExpression();
                String value = ObjectUtil.toString(condition.getValue());
                String op = condition.getOp();
                String methodName = methodMapping.get(op);
                if (CharSequenceUtil.isNotEmpty(methodName) && GXCommonUtils.checkMethodExists(Criteria.class, methodName, value)) {
                    Criteria tmpCriteria = new Criteria(fieldName);
                    GXCommonUtils.reflectCallObjectMethod(tmpCriteria, methodName, value);
                    criteria.and(tmpCriteria);
                }
            });
        }
        return buildCriteria(criteria);
    }

    /**
     * 获取指定的ElasticsearchTemplate类型对象
     *
     * @return ElasticsearchTemplate 对象
     */
    default ElasticsearchTemplate getElasticsearchTemplate() {
        return getElasticsearchTemplate(getElasticsearchTemplateName());
    }

    /**
     * 获取当前类的实体Class
     *
     * @return Class<T>
     */
    default Class<?> getGenericClassType() {
        return GXCommonUtils.getGenericClassType((Class<?>) getClass().getGenericInterfaces()[0], 0);
    }

    /**
     * 获取Spring容器中的ElasticsearchTemplate对象bean的名字
     *
     * @return bean的名字
     */
    default String getElasticsearchTemplateName() {
        return "primaryElasticsearchTemplate";
    }

    /**
     * 获取指定的ElasticsearchTemplate类型对象
     *
     * @param beanName spring容器中bean的名字
     * @return ElasticsearchTemplate 对象
     */
    default ElasticsearchTemplate getElasticsearchTemplate(String beanName) {
        if (CharSequenceUtil.isEmpty(beanName)) {
            beanName = getElasticsearchTemplateName();
        }
        ElasticsearchTemplate elasticsearchTemplate = GXSpringContextUtils.getBean(beanName, ElasticsearchTemplate.class);
        Assert.notNull(elasticsearchTemplate, "请配置ElasticsearchTemplate对象->[" + GXSpringContextUtils.getBeans(ElasticsearchTemplate.class).keySet().stream().map(name -> {
            return CharSequenceUtil.format("'{}'", name);
        }).collect(Collectors.joining(",")) + "]");
        return elasticsearchTemplate;
    }
}
