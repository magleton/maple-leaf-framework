package cn.maple.dubbo.nacos.api;

import cn.maple.core.framework.dto.protocol.req.GXQueryParamReqProtocol;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.util.GXCommonUtils;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
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
     * 根据条件获取一条数据
     *
     * @param columns     需要查询的列
     * @param condition   查询条件
     * @param targetClazz 目标类型
     * @return R
     */
    default <E> E findOneByCondition(Set<String> columns, Table<String, String, Object> condition, Class<E> targetClazz) {
        R data = findOneByCondition(columns, condition);
        return GXCommonUtils.convertSourceToTarget(data, targetClazz, null, null);
    }

    /**
     * 根据条件获取数据
     *
     * @param columns   需要查询的列
     * @param condition 查询条件
     * @return List
     */
    List<R> findByCondition(Set<String> columns, Table<String, String, Object> condition);

    /**
     * 根据条件获取数据
     *
     * @param columns   需要查询的列
     * @param condition 查询条件
     * @return List
     */
    default <E> List<E> findByCondition(Set<String> columns, Table<String, String, Object> condition, Class<E> targetClazz) {
        List<R> rList = findByCondition(columns, condition);
        return GXCommonUtils.convertSourceListToTargetList(rList, targetClazz, null, null);
    }

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
