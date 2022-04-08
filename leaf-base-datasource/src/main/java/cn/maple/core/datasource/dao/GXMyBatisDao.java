package cn.maple.core.datasource.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.datasource.util.GXDBCommonUtils;
import cn.maple.core.framework.constant.GXBuilderConstant;
import cn.maple.core.framework.dao.GXBaseDao;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

public class GXMyBatisDao<M extends GXBaseMapper<T, R>, T extends GXMyBatisModel, R extends GXBaseResDto, ID extends Serializable> extends ServiceImpl<M, T> implements GXBaseDao<T, R, ID> {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GXMyBatisDao.class);

    /**
     * 分页  返回实体对象
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return GXPagination
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        IPage<R> iPage = GXDBCommonUtils.constructPageObject(dbQueryParamInnerDto.getPage(), dbQueryParamInnerDto.getPageSize());
        String mapperMethodName = "paginate";
        Set<String> fieldSet = dbQueryParamInnerDto.getColumns();
        if (Objects.isNull(fieldSet)) {
            dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("*"));
        }
        Method mapperMethod = ReflectUtil.getMethod(baseMapper.getClass(), mapperMethodName, IPage.class, dbQueryParamInnerDto.getClass());
        if (Objects.nonNull(mapperMethod)) {
            final List<R> records = ReflectUtil.invoke(baseMapper, mapperMethod, iPage, dbQueryParamInnerDto);
            iPage.setRecords(records);
            return GXDBCommonUtils.convertPageToPaginationResDto(iPage);
        }
        Class<?>[] interfaces = baseMapper.getClass().getInterfaces();
        if (interfaces.length > 0) {
            String canonicalName = interfaces[0].getCanonicalName();
            throw new GXBusinessException(CharSequenceUtil.format("请在{}类中申明{}方法", canonicalName, mapperMethodName));
        }
        throw new GXBusinessException(CharSequenceUtil.format("请在Mapper类中申明{}方法", mapperMethodName));
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名字
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return 影响的行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
        if (Objects.isNull(condition) || condition.isEmpty()) {
            throw new GXBusinessException("更新数据需要指定条件");
        }
        return baseMapper.updateFieldByCondition(tableName, data, condition);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).condition(condition).build();
        Integer val = baseMapper.checkRecordIsExists(queryParamInnerDto);
        return Objects.nonNull(val);
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名字
     * @param dataList  数据集合
     * @return int
     */
    @Override
    @SuppressWarnings("all")
    @Transactional(rollbackFor = Exception.class)
    public Integer saveBatch(String tableName, List<Dict> dataList) {
        return baseMapper.saveBatch(tableName, dataList);
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        condition = Optional.ofNullable(condition).orElse(HashBasedTable.create());
        String pkName = getPrimaryKeyName();
        String pkMethodName = CharSequenceUtil.format("get{}", CharSequenceUtil.upperFirst(pkName));
        Object o = GXCommonUtils.reflectCallObjectMethod(entity, pkMethodName);
        String op = GXBuilderConstant.EQ;
        Class<Object> retIDClazz = GXCommonUtils.getGenericClassType(getClass(), 3);
        if (String.class.isAssignableFrom(retIDClazz)) {
            op = GXBuilderConstant.STR_EQ;
        }
        if (Objects.nonNull(o) && !CollUtil.contains(Arrays.asList("0", "", 0), o) && Objects.isNull(condition.get(pkName, op))) {
            condition.put(pkName, op, o);
        }
        if (!condition.isEmpty() && checkRecordIsExists(getTableName(), condition)) {
            UpdateWrapper<T> updateWrapper = GXDBCommonUtils.assemblyUpdateWrapper(condition);
            update(entity, updateWrapper);
        } else {
            save(entity);
        }
        String methodName = CharSequenceUtil.format("get{}", CharSequenceUtil.upperFirst(pkName));
        return Convert.convert(getIDClassType(), GXCommonUtils.reflectCallObjectMethod(entity, methodName));
    }

    /**
     * 通过条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return 列表
     */
    @Override
    public R findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return baseMapper.findOneByCondition(dbQueryParamInnerDto);
    }

    /**
     * 通过条件获取数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    @Override
    public List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return baseMapper.findByCondition(dbQueryParamInnerDto);
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
        return baseMapper.deleteSoftCondition(tableName, condition);
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
        return baseMapper.deleteCondition(tableName, condition);
    }

    /**
     * 获取表名字
     *
     * @return 数据库表的名字
     */
    @Override
    public String getTableName() {
        return GXDBCommonUtils.getTableName(GXCommonUtils.getGenericClassType(getClass(), 1));
    }

    /**
     * 获取主键标识的类型
     *
     * @return Class
     */
    @SuppressWarnings("all")
    private Class<ID> getIDClassType() {
        Class<ID> clazz = GXCommonUtils.getGenericClassType(getClass(), 3);
        return clazz;
    }

    /**
     * 获取实体的Class 对象
     *
     * @return Class
     */
    @SuppressWarnings("all")
    private String getPrimaryKeyName() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(GXCommonUtils.getGenericClassType(getClass(), 1));
        return tableInfo.getKeyProperty();
    }
}