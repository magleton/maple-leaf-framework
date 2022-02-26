package cn.maple.dubbo.nacos.api;

import cn.maple.core.framework.dto.protocol.req.GXQueryParamReqProtocol;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface GXBaseDubboApi<Q extends GXBaseReqDto, R extends GXBaseResDto, ID extends Serializable> {
    /**
     * 根据条件获取一条数据
     *
     * @param condition 查询条件
     * @return R
     */
    R findOneByCondition(Table<String, String, Object> condition);

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @return List
     */
    List<R> findByCondition(Table<String, String, Object> condition);

    /**
     * 根据条件获取一条数据
     *
     * @param columns   需要查询的列
     * @param condition 查询条件
     * @return R
     */
    R findOneByCondition(Set<String> columns, Table<String, String, Object> condition);

    /**
     * 根据条件获取数据
     *
     * @param columns   需要查询的列
     * @param condition 查询条件
     * @return List
     */
    List<R> findByCondition(Set<String> columns, Table<String, String, Object> condition);

    /**
     * 创建或者更新数据
     *
     * @param reqDto    请求参数
     * @param condition 更新条件
     * @return ID
     */
    ID updateOrCreate(Q reqDto, Table<String, String, Object> condition);

    /**
     * 分页数据
     *
     * @param reqProtocol 查询条件
     * @return 分页对象
     */
    GXPaginationResDto<R> paginate(GXQueryParamReqProtocol reqProtocol);

    /**
     * 物理删除
     *
     * @param condition 删除条件
     * @return 删除行数
     */
    Integer deleteCondition(Table<String, String, Object> condition);

    /**
     * 软删除
     *
     * @param condition 删除条件
     * @return 删除行数
     */
    Integer deleteSoftCondition(Table<String, String, Object> condition);
}
