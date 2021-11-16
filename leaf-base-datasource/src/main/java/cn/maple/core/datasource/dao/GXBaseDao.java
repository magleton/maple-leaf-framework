package cn.maple.core.datasource.dao;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.util.GXDBCommonUtils;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GXBaseDao<M extends GXBaseMapper<T, R>, T extends GXBaseEntity, R extends GXBaseResDto> extends ServiceImpl<M, T> {
    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(GXBaseDao.class);

    /**
     * 分页  返回实体对象
     *
     * @param condition        条件
     * @param mapperMethodName Mapper方法
     * @param fieldSet         需要获取的字段
     * @return GXPagination
     */
    public IPage<R> paginate(IPage<R> riPage, Table<String, String, Object> condition, String mapperMethodName, Set<String> fieldSet) {
        if (CharSequenceUtil.isEmpty(mapperMethodName)) {
            mapperMethodName = "paginate";
        }
        Method mapperMethod = ReflectUtil.getMethodByName(baseMapper.getClass(), mapperMethodName);
        if (Objects.isNull(mapperMethod)) {
            Class<?>[] interfaces = baseMapper.getClass().getInterfaces();
            if (interfaces.length > 0) {
                String canonicalName = interfaces[0].getCanonicalName();
                throw new GXBusinessException(CharSequenceUtil.format("请在{}类中实现{}方法", canonicalName, mapperMethodName));
            }
            throw new GXBusinessException(CharSequenceUtil.format("请在相应的Mapper类中实现{}方法", mapperMethodName));
        }
        final List<R> list = ReflectUtil.invoke(baseMapper, mapperMethod, riPage, condition, fieldSet);
        riPage.setRecords(list);
        return riPage;
    }

    /**
     * 通过条件获取分页数据列表
     *
     * @param tableName 表名
     * @param condition 查询条件
     * @param fieldSet  需要获取的字段
     * @return 列表
     */
    public List<R> paginate(IPage<R> page, String tableName, Table<String, String, Object> condition, Set<String> fieldSet) {
        return baseMapper.paginate(page, tableName, condition, fieldSet);
    }

    /**
     * 更新JSON字段中的某一个值
     *
     * @param clazz     Class对象
     * @param path      路径
     * @param value     值
     * @param condition 条件
     * @return boolean
     */
    public boolean updateSingleField(Class<T> clazz, String path, Object value, Table<String, String, Object> condition) {
        int index = CharSequenceUtil.indexOfIgnoreCase(path, "::");
        String mainPath = CharSequenceUtil.sub(path, 0, index);
        String subPath = CharSequenceUtil.sub(path, index + 1, path.length());
        final Dict data = Dict.create().set(mainPath, Dict.create().set(subPath, value));
        return baseMapper.updateFieldByCondition(getTableName(clazz), data, condition);
    }

    /**
     * 更新JSON字段中的某多个值
     *
     * @param clazz     Class对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    public boolean updateMultiFields(Class<T> clazz, Dict data, Table<String, String, Object> condition) {
        return baseMapper.updateFieldByCondition(getTableName(clazz), data, condition);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    public boolean checkRecordIsExists(Class<T> clazz, Table<String, String, Object> condition) {
        return baseMapper.checkRecordIsExists(getTableName(clazz), condition) == 1;
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        return baseMapper.checkRecordIsExists(tableName, condition) == 1;
    }

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    public Integer checkRecordIsUnique(Class<T> clazz, Table<String, String, Object> condition) {
        return baseMapper.checkRecordIsUnique(getTableName(clazz), condition);
    }

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    public boolean checkRecordIsUnique(String tableName, Table<String, String, Object> condition) {
        return baseMapper.checkRecordIsUnique(tableName, condition) > 1;
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param clazz     Class 对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    public boolean updateFieldByCondition(Class<T> clazz, Dict data, Table<String, String, Object> condition) {
        return baseMapper.updateFieldByCondition(getTableName(clazz), data, condition);
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param clazz    实体的Class
     * @param fieldSet 字段集合
     * @param dataList 数据集合
     * @return int
     */
    @SuppressWarnings("all")
    public Integer batchInsert(Class<T> clazz, Set<String> fieldSet, List<Dict> dataList) {
        return baseMapper.batchInsert(getTableName(clazz), fieldSet, dataList);
    }

    /**
     * 通过条件获取数据
     *
     * @param tableName 表名
     * @param condition 查询条件
     * @param fieldSet  需要获取的字段
     * @return 列表
     */
    public R findOneByCondition(String tableName, Table<String, String, Object> condition, Set<String> fieldSet) {
        return baseMapper.findOneByCondition(tableName, condition, fieldSet);
    }

    /**
     * 通过条件获取数据列表
     *
     * @param tableName 表名
     * @param condition 查询条件
     * @param fieldSet  需要获取的字段
     * @return 列表
     */
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition, Set<String> fieldSet) {
        return baseMapper.findByCondition(tableName, condition, fieldSet);
    }

    /**
     * 构造分页对象
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return 分页对象
     */
    public IPage<R> constructPageObject(Integer page, Integer pageSize) {
        int defaultCurrentPage = GXCommonConstant.DEFAULT_CURRENT_PAGE;
        int defaultPageSize = GXCommonConstant.DEFAULT_PAGE_SIZE;
        int defaultMaxPageSize = GXCommonConstant.DEFAULT_MAX_PAGE_SIZE;
        if (page < 0) {
            page = defaultCurrentPage;
        }
        if (pageSize > defaultMaxPageSize || pageSize <= 0) {
            pageSize = defaultPageSize;
        }
        return new Page<>(page, pageSize);
    }

    /**
     * 获取表名字
     *
     * @param clazz 实体的类型
     * @return 数据库表的名字
     */
    private String getTableName(Class<T> clazz) {
        return GXDBCommonUtils.getTableName(clazz);
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    public Integer deleteSoftWhere(String tableName, Table<String, String, Object> condition) {
        return baseMapper.deleteSoftWhere(tableName, condition);
    }

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    public Integer deleteWhere(String tableName, Table<String, String, Object> condition) {
        return baseMapper.deleteWhere(tableName, condition);
    }
}