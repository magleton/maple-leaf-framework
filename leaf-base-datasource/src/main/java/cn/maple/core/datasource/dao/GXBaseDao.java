package cn.maple.core.datasource.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.datasource.dto.inner.GXDBQueryParamInnerDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GXBaseDao<M extends GXBaseMapper<T, R>, T extends GXBaseEntity, R extends GXBaseResDto> extends ServiceImpl<M, T> {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GXBaseDao.class);

    /**
     * 分页  返回实体对象
     *
     * @param dbQueryParamInnerDto 查询条件
     * @param mapperMethodName     Mapper方法
     * @return GXPagination
     */
    public IPage<R> paginate(GXDBQueryParamInnerDto dbQueryParamInnerDto, String mapperMethodName) {
        IPage<R> iPage = constructPageObject(dbQueryParamInnerDto.getPage(), dbQueryParamInnerDto.getPageSize());
        if (CharSequenceUtil.isEmpty(mapperMethodName)) {
            mapperMethodName = "paginate";
        }
        Set<String> fieldSet = dbQueryParamInnerDto.getColumns();
        if (Objects.isNull(fieldSet)) {
            dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("*"));
        }
        Method mapperMethod = ReflectUtil.getMethod(baseMapper.getClass(), mapperMethodName, IPage.class, dbQueryParamInnerDto.getClass());
        if (Objects.isNull(mapperMethod)) {
            Class<?>[] interfaces = baseMapper.getClass().getInterfaces();
            if (interfaces.length > 0) {
                String canonicalName = interfaces[0].getCanonicalName();
                throw new GXBusinessException(CharSequenceUtil.format("请在{}类中实现{}方法", canonicalName, mapperMethodName));
            }
            throw new GXBusinessException(CharSequenceUtil.format("请在相应的Mapper类中实现{}方法", mapperMethodName));
        }
        final List<R> list = ReflectUtil.invoke(baseMapper, mapperMethod, iPage, dbQueryParamInnerDto);
        iPage.setRecords(list);
        return iPage;
    }

    /**
     * 通过条件获取分页数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    public IPage<R> paginate(GXDBQueryParamInnerDto dbQueryParamInnerDto) {
        IPage<R> iPage = constructPageObject(dbQueryParamInnerDto.getPage(), dbQueryParamInnerDto.getPageSize());
        if (Objects.isNull(dbQueryParamInnerDto.getColumns())) {
            dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("*"));
        }
        List<R> paginate = baseMapper.paginate(iPage, dbQueryParamInnerDto);
        iPage.setRecords(paginate);
        return iPage;
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名字
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
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
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        GXDBQueryParamInnerDto queryParamInnerDto = GXDBQueryParamInnerDto.builder()
                .tableName(tableName)
                .condition(condition)
                .build();
        return baseMapper.checkRecordIsExists(queryParamInnerDto) == 1;
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名字
     * @param dataList  数据集合
     * @return int
     */
    @SuppressWarnings("all")
    @Transactional(rollbackFor = Exception.class)
    public Integer batchInsert(String tableName, List<Dict> dataList) {
        return baseMapper.batchInsert(tableName, dataList);
    }

    /**
     * 通过条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return 列表
     */
    public R findOneByCondition(GXDBQueryParamInnerDto dbQueryParamInnerDto) {
        return baseMapper.findOneByCondition(dbQueryParamInnerDto);
    }

    /**
     * 通过条件获取数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    public List<R> findByCondition(GXDBQueryParamInnerDto dbQueryParamInnerDto) {
        return baseMapper.findByCondition(dbQueryParamInnerDto);
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
        if (Objects.isNull(page) || page < 0) {
            page = defaultCurrentPage;
        }
        if (Objects.isNull(pageSize) || pageSize > defaultMaxPageSize || pageSize <= 0) {
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