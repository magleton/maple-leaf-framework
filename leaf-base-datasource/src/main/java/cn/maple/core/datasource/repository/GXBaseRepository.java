package cn.maple.core.datasource.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import cn.maple.core.datasource.constant.GXBaseBuilderConstant;
import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.core.datasource.dto.inner.GXDBQueryParamInnerDto;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.util.GXDBCommonUtils;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class GXBaseRepository<M extends GXBaseMapper<T, R>, T extends GXBaseEntity, D extends GXBaseDao<M, T, R>, R extends GXBaseResDto, ID> {
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
    public ID create(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    public ID update(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    public ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询对象
     * @return 列表
     */
    public <E> List<E> findByCondition(GXDBQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz) {
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
                    list.add(Convert.convert(targetClazz, dict.getObj(key)));
                }
            });
            return list;
        }
        ArrayList<Dict> dictList = new ArrayList<>();
        rList.forEach(data -> {
            Dict dict = GXCommonUtils.convertSourceToDict(data);
            Dict tmpDict = Dict.create();
            for (String key : columns) {
                tmpDict.set(key, dict.getObj(key));
            }
            dictList.add(tmpDict);
        });
        return GXCommonUtils.convertSourceListToTargetList(dictList, targetClazz, null, null);
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询条件
     * @return 列表
     */
    public List<R> findByCondition(GXDBQueryParamInnerDto dbQueryInnerDto) {
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
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns) {
        GXDBQueryParamInnerDto paramInnerDto = GXDBQueryParamInnerDto.builder()
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
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition) {
        return findByCondition(tableName, condition, CollUtil.newHashSet("*"));
    }

    /**
     * 根据条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return R 返回数据
     */
    public R findOneByCondition(GXDBQueryParamInnerDto dbQueryParamInnerDto) {
        return baseDao.findOneByCondition(dbQueryParamInnerDto);
    }

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return R 返回数据
     */
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
    public R findOneByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns) {
        GXDBQueryParamInnerDto queryParamInnerDto = GXDBQueryParamInnerDto.builder()
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
    public GXPaginationResDto<R> paginate(String mapperMethodName, GXDBQueryParamInnerDto dbQueryParamInnerDto) {
        if (Objects.isNull(mapperMethodName)) {
            mapperMethodName = "paginate";
        }
        if (Objects.isNull(dbQueryParamInnerDto.getColumns())) {
            dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("*"));
        }
        Table<String, String, Object> condition = dbQueryParamInnerDto.getCondition();
        IPage<R> iPage = constructPageObject(dbQueryParamInnerDto.getPage(), dbQueryParamInnerDto.getPageSize());
        IPage<R> paginate = baseDao.paginate(iPage, condition, mapperMethodName, dbQueryParamInnerDto.getColumns());
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
        GXDBQueryParamInnerDto queryParamInnerDto = GXDBQueryParamInnerDto.builder()
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
    public GXPaginationResDto<R> paginate(GXDBQueryParamInnerDto dbQueryParamInnerDto) {
        if (Objects.isNull(dbQueryParamInnerDto.getColumns())) {
            dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("*"));
        }
        IPage<R> iPage = constructPageObject(dbQueryParamInnerDto.getPage(), dbQueryParamInnerDto.getPageSize());
        List<R> paginate = baseDao.paginate(iPage, dbQueryParamInnerDto);
        return GXDBCommonUtils.convertPageToPaginationResDto(iPage, paginate);
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
    public GXPaginationResDto<R> paginate(Integer page, Integer pageSize, String tableName, Table<String, String, Object> condition, Set<String> columns) {
        if (Objects.isNull(columns)) {
            columns = CollUtil.newHashSet("*");
        }
        GXDBQueryParamInnerDto queryParamInnerDto = GXDBQueryParamInnerDto.builder()
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
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        return baseDao.checkRecordIsExists(tableName, condition);
    }

    /**
     * 检测数据是否唯一
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return 1 不唯一 0 唯一
     */
    public boolean checkRecordIsUnique(String tableName, Table<String, String, Object> condition) {
        return baseDao.checkRecordIsUnique(tableName, condition);
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
    public boolean validateExists(Object value, String tableName, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        Table<String, String, Object> condition = HashBasedTable.create();
        if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, value.toString())) {
            condition.put(fieldName, GXBaseBuilderConstant.NUMBER_EQ, value);
        } else {
            condition.put(fieldName, GXBaseBuilderConstant.STR_EQ, value);
        }
        return checkRecordIsExists(tableName, condition);
    }

    /**
     * 验证数据的唯一性 (返回true表示数据已经存在)
     *
     * @param value                      值
     * @param tableName                  database table name
     * @param fieldName                  字段名字
     * @param constraintValidatorContext 验证上下文对象
     * @param param                      参数
     * @return boolean
     */
    public boolean validateUnique(Object value, String tableName, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) {
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        Table<String, String, Object> condition = HashBasedTable.create();
        if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, value.toString())) {
            condition.put(fieldName, GXBaseBuilderConstant.NUMBER_EQ, value);
        } else {
            condition.put(fieldName, GXBaseBuilderConstant.STR_EQ, value);
        }
        return checkRecordIsUnique(tableName, condition);
    }

    /**
     * 批量插入数据
     *
     * @param tableName 表名
     * @param fieldSet  需要插入的字段集合
     * @param dataList  数据集合
     * @return 影响行数
     */
    public Integer batchInsert(String tableName, Set<String> fieldSet, List<Dict> dataList) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 通过条件更新数据
     *
     * @param tableName 需要更新的表名
     * @param fieldSet  需要更新的字段集合
     * @param dataList  数据集合
     * @param condition 更新条件
     * @return 更新的条数
     */
    public Integer updateDataByCondition(String tableName, Set<String> fieldSet, List<Dict> dataList, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
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

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    public String getPrimaryKey() {
        return "id";
    }
}
