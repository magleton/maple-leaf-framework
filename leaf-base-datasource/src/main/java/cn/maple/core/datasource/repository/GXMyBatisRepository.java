package cn.maple.core.datasource.repository;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.TypeUtil;
import cn.maple.core.datasource.dao.GXMyBatisDao;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.datasource.util.GXDBCommonUtils;
import cn.maple.core.framework.constant.GXBuilderConstant;
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

public abstract class GXMyBatisRepository<M extends GXBaseMapper<T, R>, T extends GXMyBatisModel, D extends GXMyBatisDao<M, T, R, ID>, R extends GXBaseResDto, ID> implements GXBaseRepository<T, R, ID> {
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
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        condition.columnMap().forEach((op, columnData) -> columnData.forEach((column, value) -> setUpdateWrapper(updateWrapper, Dict.create().set("op", op).set("column", column).set("value", value))));
        return update(entity, updateWrapper);
    }

    /**
     * 设置更新条件对象的值
     *
     * @param updateWrapper MyBatis更新条件对象
     * @param condition     条件
     */
    protected void setUpdateWrapper(UpdateWrapper<T> updateWrapper, Dict condition) {
        String op = condition.getStr("op");
        String column = condition.getStr("column");
        String value = condition.getStr("value");
        Dict methodNameDict = Dict.create()
                .set(GXBuilderConstant.EQ, "eq")
                .set(GXBuilderConstant.STR_EQ, "eq")
                .set(GXBuilderConstant.NOT_EQ, "ne")
                .set(GXBuilderConstant.STR_NOT_EQ, "ne")
                .set(GXBuilderConstant.NOT_IN, "notIn")
                .set(GXBuilderConstant.STR_NOT_IN, "notIn")
                .set(GXBuilderConstant.GE, "ge")
                .set(GXBuilderConstant.GT, "gt")
                .set(GXBuilderConstant.LE, "le")
                .set(GXBuilderConstant.LT, "lt");
        String methodName = methodNameDict.getStr(op);
        if (Objects.nonNull(methodName)) {
            if (!GXCommonUtils.digitalRegularExpression(value)) {
                value = CharSequenceUtil.format("'{}'", value);
            }
            GXCommonUtils.reflectCallObjectMethod(updateWrapper, methodName, true, column, value);
        }
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
        if (Objects.isNull(condition) || condition.isEmpty()) {
            return updateOrCreate(entity, (UpdateWrapper<T>) null);
        }
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        condition.columnMap().forEach((op, columnData) -> columnData.forEach((column, value) -> setUpdateWrapper(updateWrapper, Dict.create().set("op", op).set("column", column).set("value", value))));
        return updateOrCreate(entity, updateWrapper);
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
            this.update(entity, updateWrapper);
        } else {
            this.create(entity);
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
        if (columns.size() > 1 && ClassUtil.isSimpleValueType(targetClazz)) {
            throw new GXBusinessException("接收的数据类型不正确");
        }
        List<R> rList = baseDao.findByCondition(dbQueryInnerDto);
        if (columns.size() == 1 && ClassUtil.isSimpleValueType(targetClazz)) {
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
     * @param tableName   表名字
     * @param condition   查询条件
     * @param columns     查询列
     * @param targetClazz 结果数据类型
     * @return 列表
     */
    @Override
    public <E> List<E> findByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz) {
        GXBaseQueryParamInnerDto paramInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).condition(condition).columns(columns).build();
        return findByCondition(paramInnerDto, targetClazz);
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
        GXBaseQueryParamInnerDto paramInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).condition(condition).columns(columns).build();
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
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).condition(condition).columns(columns).build();
        return findOneByCondition(queryParamInnerDto);
    }

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code findFieldByCondition("s_admin", condition1, CollUtil.newHashSet("nickname", "username"), Dict.class);}
     * </pre>
     *
     * @param tableName   表名字
     * @param condition   查询条件
     * @param columns     字段名字集合
     * @param targetClazz 值的类型
     * @return 返回指定的类型的值对象
     */
    @Override
    @SuppressWarnings("all")
    public <E> E findFieldByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz) {
        GXBaseQueryParamInnerDto paramInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(columns).condition(condition).build();
        return findFieldByCondition(paramInnerDto, targetClazz);
    }

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code
     *     GXBaseQueryParamInnerDto paramInnerDto = GXBaseQueryParamInnerDto.builder()
     *                       .tableName("s_admin")
     *                       .columns(CollUtil.newHashSet("nickname", "username"))
     *                       .condition(condition)
     *                       .build();
     *     findFieldByCondition(paramInnerDto, Dict.class);
     *     }
     * </pre>
     *
     * @param dbQueryInnerDto 查询数据
     * @param targetClazz     值的类型
     * @return 返回指定的类型的值对象
     */
    @Override
    @SuppressWarnings("all")
    public <E> E findFieldByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz) {
        Set<String> columns = dbQueryInnerDto.getColumns();
        if (columns.size() > 1 && ClassUtil.isSimpleValueType(targetClazz)) {
            throw new GXBusinessException("接收的数据类型不正确");
        }
        R one = findOneByCondition(dbQueryInnerDto);
        if (Objects.isNull(one)) {
            return null;
        }
        Dict dict = GXCommonUtils.convertSourceToDict(one);
        if (ClassUtil.isSimpleValueType(targetClazz)) {
            String[] columnNames = columns.toArray(new String[0]);
            Object retValue = dict.getObj(columnNames[0]);
            if (Objects.isNull(retValue)) {
                return null;
            }
            return (E) retValue;
        }
        Dict retData = Dict.create();
        columns.forEach(column -> {
            retData.set(column, dict.getObj(column));
        });
        return (E) retData;
    }

    /**
     * 通过ID获取一条记录
     *
     * @param tableName 表名字
     * @param id        ID值
     * @return 返回数据
     */
    @Override
    public R findOneById(String tableName, ID id) {
        return findOneById(tableName, id, CollUtil.newHashSet("*"));
    }

    /**
     * 通过ID获取一条记录
     *
     * @param tableName 表名字
     * @param id        ID值
     * @param columns   需要返回的列
     * @return 返回数据
     */
    @Override
    public R findOneById(String tableName, ID id, Set<String> columns) {
        HashBasedTable<String, String, Object> condition = HashBasedTable.create();
        condition.put("id", GXBuilderConstant.EQ, id);
        if (TypeUtil.getClass(id.getClass()).getName().equalsIgnoreCase(String.class.getName())) {
            condition.put("id", GXBuilderConstant.STR_EQ, id);
        }
        return findOneByCondition(tableName, condition, columns);
    }

    /**
     * 根据条件获取分页数据 调用自定义的mapper接口中提供的方法
     *
     * @param mapperMethodName     mapper中的方法名字
     * @param dbQueryParamInnerDto 查询信息
     * @return 分页数据
     */
    public GXPaginationResDto<R> paginate(String mapperMethodName, GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        if (Objects.isNull(mapperMethodName)) {
            mapperMethodName = "paginate";
        }
        if (Objects.isNull(dbQueryParamInnerDto.getColumns())) {
            dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("*"));
        }
        return baseDao.paginate(dbQueryParamInnerDto, mapperMethodName);
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
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().page(page).pageSize(pageSize).columns(columns).condition(condition).build();
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
        return baseDao.paginate(dbQueryParamInnerDto);
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
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().page(page).pageSize(pageSize).tableName(tableName).condition(condition).columns(columns).build();
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
    public Integer deleteSoftCondition(String tableName, Table<String, String, Object> condition) {
        return baseDao.deleteSoftCondition(tableName, condition);
    }

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteCondition(String tableName, Table<String, String, Object> condition) {
        return baseDao.deleteCondition(tableName, condition);
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
            condition.put(fieldName, GXBuilderConstant.EQ, value);
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
     * @return 影响的行数
     */
    @Override
    public Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
        if (Objects.isNull(condition) || condition.isEmpty()) {
            throw new GXBusinessException("更新数据需要指定条件");
        }
        return baseDao.updateFieldByCondition(tableName, data, condition);
    }

    /**
     * 根据条件获取所有数据
     *
     * @param clazz     表实体
     * @param condition 条件
     * @return 列表
     */
    public List<R> findByCondition(Class<T> clazz, Table<String, String, Object> condition) {
        return findByCondition(GXDBCommonUtils.getTableName(clazz), condition, CollUtil.newHashSet("*"));
    }

    /**
     * 根据条件获取数据
     *
     * @param clazz     表实体
     * @param condition 查询条件
     * @return R 返回数据
     */
    public R findOneByCondition(Class<T> clazz, Table<String, String, Object> condition) {
        return findOneByCondition(GXDBCommonUtils.getTableName(clazz), condition, CollUtil.newHashSet("*"));
    }

    /**
     * 通过条件获取单字段的数据
     * <pre>
     * {@code
     * HashBasedTable<String, String, Object> condition = HashBasedTable.create();
     * String username = getSingleField("s_admin" ,condition , "username" , String.class);
     * }
     * </pre>
     *
     * @param tableName   表名
     * @param condition   查询条件
     * @param fieldName   需要的字段名字
     * @param targetClazz 返回的字段类型
     * @return 目标类型的值
     */
    @Override
    public <E> E getSingleField(String tableName, Table<String, String, Object> condition, String fieldName, Class<E> targetClazz) {
        return findFieldByCondition(tableName, condition, CollUtil.newHashSet(fieldName), targetClazz);
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
