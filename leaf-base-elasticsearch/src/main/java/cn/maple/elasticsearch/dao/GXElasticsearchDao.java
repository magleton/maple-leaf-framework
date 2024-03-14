package cn.maple.elasticsearch.dao;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
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

public interface GXElasticsearchDao<T extends GXElasticsearchModel, ID extends Serializable> extends ElasticsearchRepository<T, ID> {
    /**
     * 根据条件查询所有满足条件的数据
     *
     * @param queryParamInnerDto 查询条件对象
     * @return 返回查询到数据列表
     */
    default List<Dict> findByCondition(GXBaseQueryParamInnerDto queryParamInnerDto) {
        queryParamInnerDto.setPage(-1);
        queryParamInnerDto.setPage(-1);
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
        queryParamInnerDto.setLimit(1);
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
        List<Dict> records = lst.stream().map(d -> Convert.convert(Dict.class, d.getObj("content"))).collect(Collectors.toList());
        long totalCount = queryData.getInt("totalHits");
        long currentPage = queryParamInnerDto.getPage();
        long pageSize = queryParamInnerDto.getPageSize();
        long pages = totalCount / pageSize;
        return new GXPaginationResDto<>(records, totalCount, pages, pageSize, currentPage);
    }

    /**
     * 指定统一查询
     *
     * @param queryParamInnerDto 查询条件
     * @return 查询到的数据
     */
    default Dict executeQuery(GXBaseQueryParamInnerDto queryParamInnerDto) {
        Query query = buildQuery(queryParamInnerDto);
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
    default Query buildQuery(GXBaseQueryParamInnerDto queryParamInnerDto) {
        Criteria criteria = conditions2Criteria(queryParamInnerDto);
        CriteriaQueryBuilder criteriaQueryBuilder = new CriteriaQueryBuilder(criteria);
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteriaQueryBuilder);
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
            criteriaQuery.addSort(sort);
        }
        // 处理分页
        int page = Optional.ofNullable(queryParamInnerDto.getPage()).orElse(-1);
        int pageSize = Optional.ofNullable(queryParamInnerDto.getPageSize()).orElse(-1);
        // 处理limit
        int limit = Optional.ofNullable(queryParamInnerDto.getLimit()).orElse(0);
        if (limit == 1) {
            page = 0;
            pageSize = 1;
        }
        if (page != -1 || pageSize != -1) {
            criteriaQuery.setPageable(PageRequest.of(page, pageSize));
        }
        return criteriaQuery;
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
        return businessCriteria(criteria);
    }

    /**
     * 业务逻辑需要自定义查询条件
     * 留着扩展使用
     *
     * @param criteria 查询条件对象
     * @return criteria
     */
    default Criteria businessCriteria(Criteria criteria) {
        return criteria;
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
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().condition(condition).build();
        Query query = buildQuery(queryParamInnerDto);
        ElasticsearchTemplate elasticsearchTemplate = getElasticsearchTemplate();
        ByQueryResponse queryResponse = elasticsearchTemplate.delete(query, getGenericClassType());
        long updated = queryResponse.getUpdated();
        if (updated > 0) {
            T save = elasticsearchTemplate.save(entity);
            Class<ID> retIDClazz = GXCommonUtils.getGenericClassType((Class<?>) getClass().getGenericInterfaces()[0], 1);
            String methodName = CharSequenceUtil.format("get{}", CharSequenceUtil.upperFirst("id"));
            return Convert.convert(retIDClazz, GXCommonUtils.reflectCallObjectMethod(save, methodName));
        }
        return null;
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
     * 删除满足条件的数据
     *
     * @param tableName 表的名字
     * @param condition 删除条件
     * @return 删除的数量
     */
    default Integer deleteCondition(String tableName, List<GXCondition<?>> condition) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().condition(condition).build();
        Query query = buildQuery(queryParamInnerDto);
        ElasticsearchTemplate elasticsearchTemplate = getElasticsearchTemplate();
        ByQueryResponse delete = elasticsearchTemplate.delete(query, getGenericClassType());
        return Math.toIntExact(delete.getDeleted());
    }
}
