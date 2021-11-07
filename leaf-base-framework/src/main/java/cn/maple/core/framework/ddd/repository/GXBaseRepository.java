package cn.maple.core.framework.ddd.repository;

import cn.maple.core.framework.dto.inner.req.GXBaseReqDto;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Set;

public interface GXBaseRepository<E extends GXBaseReqDto, R extends GXBaseResDto> {
    /**
     * 保存数据
     *
     * @param saveData 需要保存的数据
     * @return R
     */
    Integer create(E saveData);

    /**
     * 保存数据
     *
     * @param updateData 需要更新的数据
     * @return R
     */
    Integer update(E updateData);

    /**
     * 保存数据
     *
     * @param data 需要更新或者保存的数据
     * @return R
     */
    Integer updateOrCreate(E data);

    /**
     * 获取所有数据
     *
     * @param columns   需要获取的列
     * @param condition 条件
     * @return 列表
     */
    List<R> all(Set<String> columns, Table<String, String, Object> condition);

    /**
     * 获取第一条数据
     *
     * @param columns   需要获取的列
     * @param condition 条件
     * @return 列表
     */
    R first(Set<String> columns, Table<String, String, Object> condition);

    /**
     * 根据条件获取数据
     *
     * @param whereCondition 查询条件
     * @return R 返回数据
     */
    R findByCondition(Table<String, String, Object> whereCondition);

    /**
     * 分页数据
     *
     * @param limit   限制条数
     * @param columns 需要的数据列
     * @return 分页对象
     */
    GXPaginationResDto<R> paginate(Integer limit, Set<String> columns);

    /**
     * 根据条件删除
     *
     * @param whereCondition 删除条件
     * @return 影响行数
     */
    int deleteWhere(Table<String, String, Object> whereCondition);
}
