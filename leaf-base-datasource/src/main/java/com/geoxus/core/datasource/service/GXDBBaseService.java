package com.geoxus.core.datasource.service;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.datasource.dao.GXBaseDao;
import com.geoxus.core.datasource.entity.GXBaseEntity;
import com.geoxus.core.datasource.mapper.GXBaseMapper;
import com.geoxus.core.framework.dto.inner.res.GXBaseResDto;
import com.geoxus.core.framework.dto.protocol.req.GXBaseSearchReqProtocol;
import com.geoxus.core.framework.service.GXBusinessService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 业务基础Service
 *
 * @param <T>
 * @author britton chen <britton@126.com>
 */
public interface GXDBBaseService<T extends GXBaseEntity, M extends GXBaseMapper<T, R>, D extends GXBaseDao<M, T, R>, R extends GXBaseResDto>
        extends GXBusinessService, GXValidateDBExistsService, GXValidateDBUniqueService {
    /**
     * 获取BaseMapper对象
     *
     * @return M
     */
    M getBaseMapper();

    /**
     * 获取BaseDao对象
     *
     * @return D
     */
    D getBaseDao();

    /**
     * 获取实体中指定指定的值
     * <pre>
     *     {@code
     *     getSingleJSONFieldValueByDB(
     *      GoodsEntity,
     *      "ext.name",
     *      Integer.class,
     *      Dict.create().set("user_id" , 1111)
     *      )
     *     }
     * </pre>
     *
     * @param clazz     Class对象
     * @param path      路径
     * @param condition 条件
     * @return R
     */
    <R> R getSingleFieldValueByDB(Class<T> clazz, String path, Class<R> type, Dict condition);

    /**
     * 获取实体中指定指定的值
     *
     * @param clazz        Class对象
     * @param path         路径
     * @param condition    条件
     * @param defaultValue 默认值
     * @return R
     */
    <R> R getSingleFieldValueByDB(Class<T> clazz, String path, Class<R> type, Dict condition, R defaultValue);

    /**
     * 获取JSON中的多个值
     *
     * @param clazz     Class 对象
     * @param fields    字段
     * @param condition 条件
     * @return Dict
     */
    Dict getMultiFieldsValueByDB(Class<T> clazz, Dict fields, Dict condition);

    /**
     * 更新JSON字段中的某一个值
     *
     * @param clazz     Class对象
     * @param path      路径
     * @param value     值
     * @param condition 条件
     * @return boolean
     */
    boolean updateSingleField(Class<T> clazz, String path, Object value, Dict condition);

    /**
     * 更新JSON字段中的某多个值
     *
     * @param clazz     Class对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateMultiFields(Class<T> clazz, Dict data, Dict condition);

    /**
     * 根据条件获取一条记录
     *
     * @param clazz     Class对象
     * @param fieldSet  字段集合
     * @param condition 查询条件
     * @param remove    是否移除
     * @return Dict
     */
    Dict getOneByCondition(Class<T> clazz, Set<String> fieldSet, Dict condition, boolean remove);

    /**
     * 检测给定条件的记录是否存在
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    Integer checkRecordIsExists(Class<T> clazz, Dict condition);

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    Integer checkRecordIsExists(String tableName, Dict condition);

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    Integer checkRecordIsUnique(Class<T> clazz, Dict condition);

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    Integer checkRecordIsUnique(String tableName, Dict condition);

    /**
     * 修改状态
     *
     * @param status    状态
     * @param condition 条件
     * @return boolean
     */
    boolean modifyStatus(int status, Dict condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param clazz     Class 对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    boolean updateFieldByCondition(Class<T> clazz, Dict data, Dict condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param clazz     Class 对象
     * @param status    状态
     * @param condition 更新条件
     * @return boolean
     */
    boolean updateStatusByCondition(Class<T> clazz, int status, Dict condition);

    /**
     * 通过SQL语句批量插入数据
     *
     * @param clazz    实体的Class
     * @param fieldSet 字段集合
     * @param dataList 数据集合
     * @return int
     */
    Integer batchInsertBySQL(Class<T> clazz, Set<String> fieldSet, List<Dict> dataList);

    /**
     * 获取表中的指定字段
     *
     * @param clazz     Class对象
     * @param fieldSet  字段集合
     * @param condition 查询条件
     * @return Dict
     */
    Dict getFieldValueBySQL(Class<T> clazz, Set<String> fieldSet, Dict condition, boolean remove);

    /**
     * 获取表中的指定字段
     *
     * @param tableName 表名
     * @param fieldSet  字段集合
     * @param condition 更新条件
     * @param remove    是否移除
     * @return Dict
     */
    Dict getFieldValueBySQL(String tableName, Set<String> fieldSet, Dict condition, boolean remove);

    /**
     * 记录原表的数据到历史表里面
     *
     * @param originTableName  原表名
     * @param historyTableName 历史表名字
     * @param condition        条件
     * @param appendData       附加信息
     * @return boolean
     */
    boolean recordModificationHistory(String originTableName, String historyTableName, Dict condition, Dict appendData);

    /**
     * 通过表明获取模型ID
     *
     * @param clazz 实体的Class
     * @return int
     */
    int getCoreModelIdByTableName(Class<T> clazz);

    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    <R> IPage<R> listOrSearchPage(GXBaseSearchReqProtocol searchReqDto);

    /**
     * 列表或者搜索(分页)
     *
     * @param param 参数
     * @return GXPagination
     */
    <R> IPage<R> listOrSearchPage(Dict param);

    /**
     * 内容详情
     *
     * @param param 参数
     * @return Dict
     */
    Dict detail(Dict param);

    /**
     * 批量更新status字段
     *
     * @param param 参数
     * @return boolean
     */
    boolean batchUpdateStatus(Dict param);

    /**
     * 通过条件获取配置信息
     *
     * @param condition 条件
     * @return Dict
     */
    Dict getDataByCondition(Dict condition, String... fields);

    /**
     * 获取分页对象信息
     *
     * @param pageInfo 参数
     * @return IPage
     */
    <R> IPage<R> constructPageObjectFromParam(Dict pageInfo);

    /**
     * 获取分页信息
     *
     * @param param 查询参数
     * @return GXPagination
     */
    <R> IPage<R> generatePage(Dict param);

    /**
     * 分页  返回实体对象
     *
     * @param param            参数
     * @param mapperMethodName Mapper方法
     * @return GXPagination
     */
    <R> IPage<R> generatePage(Dict param, String mapperMethodName);

    /**
     * 获取记录的父级path
     *
     * @param parentId   父级ID
     * @param appendSelf 　是否将parentId附加到返回结果上面
     * @return String
     */
    String getParentPath(Class<T> clazz, Long parentId, boolean appendSelf);

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    String getPrimaryKey();
}
