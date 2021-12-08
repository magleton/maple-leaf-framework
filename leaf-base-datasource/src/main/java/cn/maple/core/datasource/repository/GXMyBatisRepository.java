package cn.maple.core.datasource.repository;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import cn.maple.core.datasource.constant.GXBuilderConstant;
import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.util.GXDBCommonUtils;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.ddd.repository.GXBaseRepository;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidatorContext;
import java.util.*;

public abstract class GXMyBatisRepository<M extends GXBaseMapper<T, R>, T extends GXBaseEntity, D extends GXBaseDao<M, T, R>, R extends GXBaseResDto, ID>
        implements GXBaseRepository<T, R, ID> {
    /**
     * 基础DAO
     */
    @SuppressWarnings("all")
    @Autowired
    protected D baseDao;

    /**
     * 保存数据
     *
     * @param entity    需要保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    @Override
    public ID create(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity 需要保存的数据
     * @return ID
     */
    @SuppressWarnings("all")
    @Override
    public ID create(T entity) {
        baseDao.save(entity);
        return (ID) GXCommonUtils.reflectCallObjectMethod(entity, "getId");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    @Override
    public ID update(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity        需要更新的数据
     * @param updateWrapper 更新条件
     * @return ID
     */
    @SuppressWarnings("all")
    public ID update(T entity, UpdateWrapper<T> updateWrapper) {
        if (Objects.isNull(updateWrapper)) {
            throw new GXBusinessException("请传递更新对象");
        }
        baseDao.update(entity, updateWrapper);
        return (ID) GXCommonUtils.reflectCallObjectMethod(entity, "getId");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity        需要更新或者保存的数据
     * @param updateWrapper 更新条件
     * @return ID
     */
    @SuppressWarnings("all")
    public ID updateOrCreate(T entity, UpdateWrapper<T> updateWrapper) {
        if (Objects.nonNull(updateWrapper)) {
            baseDao.update(entity, updateWrapper);
        } else {
            baseDao.save(entity);
        }
        return (ID) GXCommonUtils.reflectCallObjectMethod(entity, "getId");
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询对象
     * @return 列表
     */
    @Override
    public <E> List<E> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz) {
        Set<String> columns = dbQueryInnerDto.getColumns();
        if (columns.size() > 1 && ClassUtil.isSimpleTypeOrArray(targetClazz)) {
            throw new GXBusinessException("接收的数据类型不正确");
        }
        List<R> rList = baseDao.findByCondition(dbQueryInnerDto);
        if (columns.size() == 1 && ClassUtil.isSimpleTypeOrArray(targetClazz)) {
            ArrayList<E> list = new ArrayList<>();
            rList.forEach(data -> {
                Dict dict = GXCommonUtils.convertSourceToDict(data);
                for (String key : columns) {
                    Object obj = Optional.ofNullable(dict.getObj(key)).orElse(dict.getObj(GXCommonUtils.toCamelCase(key)));
                    list.add(Convert.convert(targetClazz, obj));
                }
            });
            return list;
        }
        ArrayList<Dict> dictList = new ArrayList<>();
        rList.forEach(data -> {
            Dict dict = GXCommonUtils.convertSourceToDict(data);
            Dict tmpDict = Dict.create();
            for (String key : columns) {
                Object obj = Optional.ofNullable(dict.getObj(key)).orElse(dict.getObj(GXCommonUtils.toCamelCase(key)));
                tmpDict.set(key, obj);
            }
            dictList.add(tmpDict);
        });
        String methodName = dbQueryInnerDto.getMethodName();
        CopyOptions copyOptions = dbQueryInnerDto.getCopyOptions();
        return GXCommonUtils.convertSourceListToTargetList(dictList, targetClazz, methodName, copyOptions);
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询条件
     * @return 列表
     */
    @Override
    public List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto) {
        return baseDao.findByCondition(dbQueryInnerDto);
    }

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @param columns   需要获取的列
     * @return 列表
     */
    @Override
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns) {
        GXBaseQueryParamInnerDto paramInnerDto = GXBaseQueryParamInnerDto.builder()
                .tableName(tableName)
                .condition(condition)
                .columns(columns)
                .build();
        return findByCondition(paramInnerDto);
    }

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @return 列表
     */
    @Override
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition) {
        return findByCondition(tableName, condition, CollUtil.newHashSet("*"));
    }

    /**
     * 根据条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return R 返回数据
     */
    @Override
    public R findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return baseDao.findOneByCondition(dbQueryParamInnerDto);
    }

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return R 返回数据
     */
    @Override
    public R findOneByCondition(String tableName, Table<String, String, Object> condition) {
        return findOneByCondition(tableName, condition, CollUtil.newHashSet("*"));
    }

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @param columns   需要查询的列
     * @return R 返回数据
     */
    @Override
    public R findOneByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder()
                .tableName(tableName)
                .condition(condition)
                .columns(columns)
                .build();
        return findOneByCondition(queryParamInnerDto);
    }

    /**
     * 根据条件获取分页数据 调用自定义的mapper接口中提供的方法
     *
     * @param mapperMethodName     mapper中的方法名字
     * @param dbQueryParamInnerDto 查询信息
     * @return
     */
    public GXPaginationResDto<R> paginate(String mapperMethodName, GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        if (Objects.isNull(mapperMethodName)) {
            mapperMethodName = "paginate";
        }
        if (Objects.isNull(dbQueryParamInnerDto.getColumns())) {
            dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("*"));
        }
        IPage<R> paginate = baseDao.paginate(dbQueryParamInnerDto, mapperMethodName);
        return GXDBCommonUtils.convertPageToPaginationResDto(paginate);
    }

    /**
     * 根据条件获取分页数据
     *
     * @param page      当前页
     * @param pageSize  每页大小
     * @param condition 查询条件
     * @param columns   需要的数据列
     * @return 分页对象
     */
    public GXPaginationResDto<R> paginate(Integer page, Integer pageSize, Table<String, String, Object> condition, String mapperMethodName, Set<String> columns) {
        if (Objects.isNull(mapperMethodName)) {
            mapperMethodName = "paginate";
        }
        if (Objects.isNull(columns)) {
            columns = CollUtil.newHashSet("*");
        }
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder()
                .page(page)
                .pageSize(pageSize)
                .columns(columns)
                .condition(condition)
                .build();
        return paginate(mapperMethodName, queryParamInnerDto);
    }

    /**
     * 根据条件获取分页数据
     *
     * @param dbQueryParamInnerDto 条件查询
     * @return 分页数据
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        if (Objects.isNull(dbQueryParamInnerDto.getColumns())) {
            dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("*"));
        }
        IPage<R> paginate = baseDao.paginate(dbQueryParamInnerDto);
        return GXDBCommonUtils.convertPageToPaginationResDto(paginate);
    }

    /**
     * 根据条件获取分页数据
     *
     * @param page      当前页
     * @param pageSize  每页大小
     * @param tableName 表名字
     * @param condition 查询条件
     * @param columns   需要的数据列
     * @return 分页对象
     */
    @Override
    public GXPaginationResDto<R> paginate(Integer page, Integer pageSize, String tableName, Table<String, String, Object> condition, Set<String> columns) {
        if (Objects.isNull(columns)) {
            columns = CollUtil.newHashSet("*");
        }
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder()
                .page(page)
                .pageSize(pageSize)
                .tableName(tableName)
                .condition(condition)
                .columns(columns)
                .build();
        return paginate(queryParamInnerDto);
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteSoftWhere(String tableName, Table<String, String, Object> condition) {
        return baseDao.deleteSoftWhere(tableName, condition);
    }

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteWhere(String tableName, Table<String, String, Object> condition) {
        return baseDao.deleteWhere(tableName, condition);
    }

    /**
     * 检测数据是否存在
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return 1 存在 0 不存在
     */
    @Override
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        return baseDao.checkRecordIsExists(tableName, condition);
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
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        Table<String, String, Object> condition = HashBasedTable.create();
        if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, value.toString())) {
            condition.put(fieldName, GXBuilderConstant.NUMBER_EQ, value);
        } else {
            condition.put(fieldName, GXBuilderConstant.STR_EQ, value);
        }
        return checkRecordIsExists(tableName, condition);
    }

    /**
     * 批量插入数据
     *
     * @param tableName 表名
     * @param dataList  数据集合
     * @return 影响行数
     */
    @Override
    public Integer batchInsert(String tableName, List<Dict> dataList) {
        return baseDao.batchInsert(tableName, dataList);
    }

    /**
     * 通过条件更新数据
     *
     * @param tableName 需要更新的表名
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return 更新的条数
     */
    @Override
    public boolean updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
        if (Objects.isNull(condition) || condition.isEmpty()) {
            throw new GXBusinessException("更新数据需要指定条件");
        }
        return baseDao.updateFieldByCondition(tableName, data, condition);
    }

    /**
     * 构造分页对象
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return 分页对象
     */
    public IPage<R> constructPageObject(Integer page, Integer pageSize) {
        return baseDao.constructPageObject(page, pageSize);
    }
}
