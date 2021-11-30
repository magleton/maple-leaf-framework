package cn.maple.core.datasource.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.core.datasource.dto.inner.GXDBQueryParamInnerDto;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.repository.GXBaseRepository;
import cn.maple.core.datasource.service.GXDBBaseService;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.service.impl.GXBusinessServiceImpl;
import cn.maple.core.framework.util.GXCommonUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * 业务基础Service
 *
 * @param <P> 仓库对象类型
 * @param <R> 响应数据类型
 */
public class GXDBBaseServiceImpl<P extends GXBaseRepository<M, T, D, R, ID>,
        M extends GXBaseMapper<T, R>,
        T extends GXBaseEntity,
        D extends GXBaseDao<M, T, R>,
        R extends GXBaseResDto,
        ID>
        extends GXBusinessServiceImpl
        implements GXDBBaseService<P, M, T, D, R, ID> {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = GXCommonUtils.getLogger(GXDBBaseServiceImpl.class);

    /**
     * 仓库类型
     */
    @Autowired
    @SuppressWarnings("all")
    protected P repository;

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        return repository.checkRecordIsExists(tableName, condition);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    @Override
    public boolean updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
        return false;
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名
     * @param dataList  数据集合
     * @return int
     */
    @SuppressWarnings("all")
    @Override
    public Integer batchInsert(String tableName, List<Dict> dataList) {
        return repository.batchInsert(tableName, dataList);
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param queryParamReqDto 参数
     * @return GXPagination
     */
    @Override
    public GXPaginationResDto<R> paginate(GXDBQueryParamInnerDto queryParamReqDto) {
        queryParamReqDto.setColumns(CollUtil.newHashSet("*"));
        return repository.paginate("paginate", queryParamReqDto);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param searchReqDto 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(GXDBQueryParamInnerDto searchReqDto) {
        return repository.findByCondition(searchReqDto);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param searchReqDto 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(GXDBQueryParamInnerDto searchReqDto) {
        return repository.findOneByCondition(searchReqDto);
    }

    /**
     * 创建数据
     *
     * @param entity 数据实体
     * @return ID
     */
    @Override
    public ID create(T entity) {
        return repository.create(entity);
    }

    /**
     * 更新数据
     *
     * @param entity    数据实体
     * @param condition 更新条件
     * @return ID
     */
    @Override
    public ID update(T entity, Table<String, String, Object> condition) {
        return repository.update(entity, condition);
    }

    /**
     * 更新数据
     *
     * @param entity        数据实体
     * @param updateWrapper 更新条件
     * @return ID
     */
    @Override
    public ID update(T entity, UpdateWrapper<T> updateWrapper) {
        return repository.update(entity, updateWrapper);
    }

    /**
     * 创建或者更新
     *
     * @param entity    数据实体
     * @param condition 更新条件
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        return repository.updateOrCreate(entity, condition);
    }

    /**
     * 创建或者更新
     *
     * @param entity        数据实体
     * @param updateWrapper 更新条件
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity, UpdateWrapper<T> updateWrapper) {
        return repository.updateOrCreate(entity, updateWrapper);
    }

    /**
     * 实现验证注解(返回true表示数据已经存在)
     *
     * @param value                      The value to check for
     * @param tableName                  database table name
     * @param fieldName                  The name of the field for which to check if the value exists
     * @param constraintValidatorContext The ValidatorContext
     * @param param                      param
     * @return boolean
     */
    @Override
    public boolean validateExists(Object value, String tableName, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        return repository.validateExists(value, tableName, fieldName, constraintValidatorContext, param);
    }

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    @Override
    public String getPrimaryKey() {
        return "id";
    }
}
