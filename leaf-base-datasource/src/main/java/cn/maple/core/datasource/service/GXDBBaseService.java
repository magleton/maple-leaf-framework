package cn.maple.core.datasource.service;

import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.protocol.req.GXBaseSearchReqProtocol;
import cn.maple.core.framework.service.GXBusinessService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 业务基础Service
 *
 * @param <T> 实体对象类型
 * @param <M> Mybatis Mapper对象类型
 * @param <D> DAO对象类型
 * @param <R> 查询的返回对象类型
 * @author britton chen <britton@126.com>
 */
public interface GXDBBaseService<M extends GXBaseMapper<T, R>, T extends GXBaseEntity, D extends GXBaseDao<M, T, R>, R extends GXBaseResDto>
        extends GXBusinessService, GXDBCommonService, GXValidateDBExistsService, GXValidateDBUniqueService {
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
     * 通过SQL更新表中的数据
     *
     * @param clazz     Class 对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    boolean updateFieldByCondition(Class<T> clazz, Dict data, Dict condition);

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
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    IPage<R> listOrSearchPage(GXBaseSearchReqProtocol searchReqDto);

    /**
     * 列表或者搜索(分页)
     *
     * @param param 参数
     * @return GXPagination
     */
    IPage<R> listOrSearchPage(Dict param);

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
    IPage<R> constructMyBatisPageObject(Dict pageInfo);

    /**
     * 获取分页信息
     *
     * @param param 查询参数
     * @return GXPagination
     */
    IPage<R> generatePage(Dict param);

    /**
     * 分页  返回实体对象
     *
     * @param param            参数
     * @param mapperMethodName Mapper方法
     * @return GXPagination
     */
    IPage<R> generatePage(Dict param, String mapperMethodName);

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    String getPrimaryKey();
}
