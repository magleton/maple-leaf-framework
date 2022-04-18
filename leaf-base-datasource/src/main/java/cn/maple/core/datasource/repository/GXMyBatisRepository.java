package cn.maple.core.datasource.repository;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import cn.maple.core.datasource.dao.GXMyBatisDao;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.ddd.repository.GXBaseRepository;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.condition.GXConditionEQ;
import cn.maple.core.framework.dto.inner.condition.GXConditionStrEQ;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.res.GXBaseDBResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.util.GXValidatorUtils;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.*;

public abstract class GXMyBatisRepository<M extends GXBaseMapper<T, R>, T extends GXMyBatisModel, D extends GXMyBatisDao<M, T, R, ID>, R extends GXBaseDBResDto, ID extends Serializable> implements GXBaseRepository<T, R, ID> {
    /**
     * 日志对象
     */
    @SuppressWarnings("all")
    private static final Logger LOGGER = GXCommonUtils.getLogger(GXMyBatisRepository.class);

    /**
     * 基础DAO
     */
    @SuppressWarnings("all")
    @Autowired
    protected D baseDao;

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ID updateOrCreate(T entity, List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
        GXValidatorUtils.validateEntity(entity);
        return baseDao.updateOrCreate(entity, condition);
    }

    /**
     * 创建或者更新
     *
     * @param entity 数据实体
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ID updateOrCreate(T entity) {
        return updateOrCreate(entity, CollUtil.newArrayList());
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询对象
     * @param extraData       额外数据
     * @return 列表
     */
    @Override
    public <E> List<E> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz, Dict extraData) {
        Set<String> columns = dbQueryInnerDto.getColumns();
        if (columns.size() > 1 && ClassUtil.isSimpleValueType(targetClazz)) {
            throw new GXBusinessException("接收的数据类型不正确");
        }
        List<R> rList = findByCondition(dbQueryInnerDto);
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
        return GXCommonUtils.convertSourceListToTargetList(dictList, targetClazz, methodName, copyOptions, extraData);
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询对象
     * @param targetClazz     目标对象类型
     * @return 列表
     */
    @Override
    public <E> List<E> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz) {
        return findByCondition(dbQueryInnerDto, targetClazz, Dict.create());
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
    public <E> List<E> findByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns, Class<E> targetClazz) {
        Assert.notNull(condition, "条件不能为null");
        GXBaseQueryParamInnerDto paramInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).condition(condition).columns(columns).build();
        return findByCondition(paramInnerDto, targetClazz, Dict.create());
    }

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    @Override
    public List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        List<R> rs = baseDao.findByCondition(dbQueryParamInnerDto);
        rs.forEach(r -> callResultMethod(dbQueryParamInnerDto, r));
        return rs;
    }

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @return 列表
     */
    @Override
    public List<R> findByCondition(String tableName, List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).condition(condition).columns(CollUtil.newHashSet("*")).build();
        return findByCondition(queryParamInnerDto);
    }

    /**
     * 根据条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return R 返回数据
     */
    @Override
    public R findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        if (CharSequenceUtil.isEmpty(dbQueryParamInnerDto.getTableName())) {
            dbQueryParamInnerDto.setTableName(getTableName());
        }
        R r = baseDao.findOneByCondition(dbQueryParamInnerDto);
        if (Objects.nonNull(r)) {
            callResultMethod(dbQueryParamInnerDto, r);
        }
        return r;
    }

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return R 返回数据
     */
    @Override
    public R findOneByCondition(String tableName, List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
        return findOneByCondition(tableName, condition, null);
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
    public R findOneByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns) {
        Assert.notNull(condition, "条件不能为null");
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
    public <E> List<E> findFieldByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns, Class<E> targetClazz) {
        Assert.notNull(condition, "条件不能为null");
        GXBaseQueryParamInnerDto paramInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(columns).condition(condition).build();
        return findFieldByCondition(paramInnerDto, targetClazz, Dict.create());
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
     * @param extraData       额外参数
     * @return 返回指定的类型的值对象
     */
    @Override
    @SuppressWarnings("all")
    public <E> List<E> findFieldByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz, Dict extraData) {
        Set<String> columns = dbQueryInnerDto.getColumns();
        if (columns.size() > 1 && ClassUtil.isSimpleValueType(targetClazz)) {
            throw new GXBusinessException("接收的数据类型不正确");
        }
        List<R> rList = findByCondition(dbQueryInnerDto);
        if (ClassUtil.isSimpleValueType(targetClazz)) {
            String[] columnNames = columns.toArray(new String[0]);
            String columnName = CharSequenceUtil.toCamelCase(columnNames[0]);
            List<E> retList = new ArrayList<>();
            rList.forEach(r -> {
                Dict dict = GXCommonUtils.convertSourceToTarget(r, Dict.class, null, null);
                if (Objects.nonNull(dict.get(columnName))) {
                    retList.add((E) dict.get(columnName));
                }
            });
            return retList;
        }
        List<Dict> retListDict = new ArrayList<>();
        rList.forEach(r -> {
            Dict dict = GXCommonUtils.convertSourceToTarget(r, Dict.class, null, null);
            Dict tmpDict = Dict.create();
            columns.forEach(column -> {
                tmpDict.set(column, dict.getObj(CharSequenceUtil.toCamelCase(column)));
            });
            retListDict.add(tmpDict);
        });
        return GXCommonUtils.convertSourceListToTargetList(retListDict, targetClazz, dbQueryInnerDto.getMethodName(), null, extraData);
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
        GXCondition<?> condition;
        String pkFieldName = getPrimaryKeyName();
        if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, id.toString())) {
            condition = new GXConditionEQ(tableName, pkFieldName, (Long) id);
        } else {
            condition = new GXConditionStrEQ(tableName, pkFieldName, id.toString());
        }
        return findOneByCondition(tableName, List.of(condition), columns);
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
        GXPaginationResDto<R> paginate = baseDao.paginate(dbQueryParamInnerDto);
        paginate.getRecords().forEach(r -> callResultMethod(dbQueryParamInnerDto, r));
        return paginate;
    }

    /**
     * 根据条件获取分页数据
     *
     * @param tableName 表名字
     * @param page      当前页
     * @param pageSize  每页大小
     * @param condition 查询条件
     * @param columns   需要的数据列
     * @return 分页对象
     */
    @Override
    public GXPaginationResDto<R> paginate(String tableName, Integer page, Integer pageSize, List<GXCondition<?>> condition, Set<String> columns) {
        Assert.notNull(condition, "条件不能为null");
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
    public Integer deleteSoftCondition(String tableName, List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
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
    public Integer deleteCondition(String tableName, List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
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
    public boolean checkRecordIsExists(String tableName, List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
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
            throw new GXBusinessException(CharSequenceUtil.format("请指定表名 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        GXCondition<?> condition;
        if (ReUtil.isMatch(GXCommonConstant.DIGITAL_REGULAR_EXPRESSION, value.toString())) {
            condition = new GXConditionEQ(tableName, fieldName, (Long) value);
        } else {
            condition = new GXConditionStrEQ(tableName, fieldName, value.toString());
        }

        return checkRecordIsExists(tableName, List.of(condition));
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
    public Integer updateFieldByCondition(String tableName, List<GXUpdateField<?>> data, List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
        if (condition.isEmpty()) {
            throw new GXBusinessException("更新数据需要指定条件");
        }
        return baseDao.updateFieldByCondition(tableName, data, condition);
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
    public <E> E findOneSingleFieldByCondition(String tableName, List<GXCondition<?>> condition, String fieldName, Class<E> targetClazz) {
        Assert.notNull(condition, "条件不能为null");
        List<E> eList = findFieldByCondition(tableName, condition, CollUtil.newHashSet(fieldName), targetClazz);
        if (eList.isEmpty()) {
            return null;
        }
        return eList.get(0);
    }

    /**
     * 获取 Primary Key
     *
     * @param entity 实体对象
     * @return String
     */
    @Override
    public String getPrimaryKeyName(T entity) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        return tableInfo.getKeyProperty();
    }

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    @Override
    public String getPrimaryKeyName() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(GXCommonUtils.getGenericClassType(getClass(), 1));
        return tableInfo.getKeyProperty();
    }

    /**
     * 获取实体的表名字
     *
     * @param entity 实体对象
     * @return 实体表名字
     */
    @Override
    public String getTableName(T entity) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        return tableInfo.getTableName();
    }

    /**
     * 通过泛型标识获取实体的表名字
     *
     * @return 数据库表名字
     */
    @Override
    public String getTableName() {
        Class<?> entityClass = GXCommonUtils.getGenericClassType(getClass(), 1);
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        return tableInfo.getTableName();
    }

    /**
     * 条用数据库查询的结果集回调处理函数
     *
     * @param dbQueryParamInnerDto 查询参数
     * @param r                    结果集数据
     */
    private void callResultMethod(GXBaseQueryParamInnerDto dbQueryParamInnerDto, R r) {
        String methodName = dbQueryParamInnerDto.getMethodName();
        if (CharSequenceUtil.isBlank(methodName)) {
            methodName = "customizeProcess";
        }
        Object extraData = Optional.ofNullable(dbQueryParamInnerDto.getExtraData()).orElse(Dict.create());
        GXCommonUtils.reflectCallObjectMethod(r, methodName, extraData);
    }
}
