package cn.maple.core.framework.ddd.repository;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.inner.GXBaseSEParamInnerDto;
import cn.maple.core.framework.exception.GXBusinessException;

import java.util.List;
import java.util.Set;

/**
 * 外置搜索引擎基础操作库
 *
 * @author britton gapleaf@163.com
 * @since 2022-01-26
 */
public interface GXBaseSERepository {
    /**
     * 通过条件获取数据
     *
     * @param dataIndexesParamInnerDto 索引数据
     * @return 列表
     */
    default <R> List<R> search(GXBaseSEParamInnerDto dataIndexesParamInnerDto, Class<R> targetClass) {
        throw new GXBusinessException("请自定义实现!");
    }

    /**
     * 新增或者修改索引数据
     *
     * @param indexName 索引名字
     * @param data      索引数据
     * @return 新增是否成功
     */
    default boolean updateOrCreate(String indexName, Dict data) {
        throw new GXBusinessException("请自定义实现!");
    }

    /**
     * 删除索引数据
     *
     * @param indexName 索引名字
     * @param indexKey  索引的键名
     * @return 索引数据是否删除成功
     */
    default boolean removeData(String indexName, String indexKey) {
        throw new GXBusinessException("请自定义实现!");
    }

    /**
     * 删除索引
     *
     * @param indexName 索引名字
     * @param dataType  数据类型 1、JSON  2、HASH
     * @param prefix    需要索引的文档前缀  user: doc:
     *                  eg:
     *                  user:001
     *                  doc:001
     * @return 删除索引是否成功
     */
    default boolean deleteIndex(String indexName, String dataType, Set<String> prefix) {
        throw new GXBusinessException("请自定义实现!");
    }
}
