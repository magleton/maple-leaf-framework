package cn.maple.core.framework.ddd.repository;

import com.google.common.collect.Table;

import java.util.List;
import java.util.Set;

public interface GXBaseRepository {
    /**
     * 获取所有数据
     *
     * @param columns   需要获取的列
     * @param condition 条件
     * @param <R>       数据的类型
     * @return 列表
     */
    <R> List<R> all(Set<String> columns, Table<String, String, Object> condition);
}
