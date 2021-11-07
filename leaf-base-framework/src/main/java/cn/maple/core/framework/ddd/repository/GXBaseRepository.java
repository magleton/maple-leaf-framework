package cn.maple.core.framework.ddd.repository;

import cn.maple.core.framework.ddd.presenter.GXBasePresenter;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
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

    /**
     * 根据条件获取数据
     *
     * @param whereCondition 查询条件
     * @param <R>            数据类型
     * @return R 返回数据
     */
    <R> R findByCondition(Table<String, String, Object> whereCondition);

    /**
     * 设置presenter对象
     *
     * @param presenter presenter对象
     */
    GXBaseRepository setPresenter(GXBasePresenter presenter);

    /**
     * 分页数据
     *
     * @param limit   限制条数
     * @param columns 需要的数据列
     * @param <R>     类型
     * @return 分页对象
     */
    <R> GXPaginationResDto<R> paginate(Integer limit, Set<String> columns);

    /**
     * 根据条件删除
     *
     * @param whereCondition 删除条件
     * @return 影响行数
     */
    int deleteWhere(Table<String, String, Object> whereCondition);
}
