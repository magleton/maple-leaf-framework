package cn.maple.core.datasource.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.datasource.constant.GXBaseBuilderConstant;
import cn.maple.core.datasource.dao.GXBaseDao;
import cn.maple.core.datasource.entity.GXBaseEntity;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.service.GXDBBaseService;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.protocol.req.GXBaseSearchReqProtocol;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.pojo.GXBusinessStatusCode;
import cn.maple.core.framework.util.GXCommonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidatorContext;
import java.util.*;

/**
 * 业务基础Service
 *
 * @param <T> 实体对象类型
 * @param <M> Mybatis Mapper对象类型
 * @param <D> DAO对象类型
 * @param <R> 查询的返回对象类型
 * @author britton chen <britton@126.com>
 */
public class GXDBBaseServiceImpl<M extends GXBaseMapper<T, R>, T extends GXBaseEntity, D extends GXBaseDao<M, T, R>, R extends GXBaseResDto>
        extends GXDBCommonServiceImpl
        implements GXDBBaseService<M, T, D, R> {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = GXCommonUtils.getLogger(GXDBBaseServiceImpl.class);

    /**
     * 基础DAO
     */
    @Autowired
    @SuppressWarnings("all")
    protected D baseDao;

    /**
     * 更新JSON字段中的某一个值
     *
     * @param clazz     Class对象
     * @param path      路径
     * @param value     值
     * @param condition 条件
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSingleField(Class<T> clazz, String path, Object value, Dict condition) {
        return baseDao.updateSingleField(clazz, path, value, condition);
    }

    /**
     * 更新JSON字段中的某多个值
     *
     * @param clazz     Class对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMultiFields(Class<T> clazz, Dict data, Dict condition) {
        return baseDao.updateMultiFields(clazz, data, condition);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    @Override
    public Integer checkRecordIsExists(Class<T> clazz, Dict condition) {
        return baseDao.checkRecordIsExists(clazz, condition);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public Integer checkRecordIsExists(String tableName, Dict condition) {
        return baseDao.checkRecordIsExists(tableName, condition);
    }

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param clazz     实体的Class
     * @param condition 条件
     * @return int
     */
    @Override
    public Integer checkRecordIsUnique(Class<T> clazz, Dict condition) {
        return baseDao.checkRecordIsUnique(clazz, condition);
    }

    /**
     * 检测给定条件的记录是否唯一
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public Integer checkRecordIsUnique(String tableName, Dict condition) {
        return baseDao.checkRecordIsUnique(tableName, condition);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param clazz     Class 对象
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return boolean
     */
    @Override
    public boolean updateFieldByCondition(Class<T> clazz, Dict data, Dict condition) {
        return baseDao.updateFieldByCondition(clazz, data, condition);
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
    @Override
    public Integer batchInsertBySQL(Class<T> clazz, Set<String> fieldSet, List<Dict> dataList) {
        return baseDao.batchInsert(clazz, fieldSet, dataList);
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    @Override
    public IPage<R> listOrSearchPage(GXBaseSearchReqProtocol searchReqDto) {
        final Dict param = Dict.create();
        if (Objects.nonNull(searchReqDto.getPagingInfo())) {
            param.set("pagingInfo", searchReqDto.getPagingInfo());
        }
        if (Objects.nonNull(searchReqDto.getSearchCondition())) {
            param.set(GXBaseBuilderConstant.SEARCH_CONDITION_NAME, searchReqDto.getSearchCondition());
        }
        return generatePage(param);
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param param 参数
     * @return GXPagination
     */
    @Override
    public IPage<R> listOrSearchPage(Dict param) {
        return generatePage(param);
    }

    /**
     * 批量更新status字段
     *
     * @param param 参数
     * @return boolean
     */
    @Override
    public boolean batchUpdateStatus(Dict param) {
        final List<Long> ids = Convert.convert(new TypeReference<List<Long>>() {
        }, param.getObj(getPrimaryKey()));
        if (null == ids) {
            return false;
        }
        final List<T> updateEntities = new ArrayList<>();
        for (Long id : ids) {
            T entity = baseDao.getById(id);
            final Object status = ReflectUtil.invoke(entity, "getStatus");
            Long entityCurrentStatus = 0L;
            if (null != status) {
                entityCurrentStatus = Convert.toLong(status, 0L);
            }
            if ((entityCurrentStatus & GXBusinessStatusCode.DELETED.getCode()) != GXBusinessStatusCode.DELETED.getCode()) {
                ReflectUtil.invoke(entity, "setStatus", GXBusinessStatusCode.DELETED.getCode());
                updateEntities.add(entity);
            }
        }
        return updateEntities.isEmpty() || baseDao.updateBatchById(updateEntities);
    }

    /**
     * 通过条件获取配置信息
     *
     * @param condition 条件
     * @return Dict
     */
    @Override
    public Dict getDataByCondition(Dict condition, String... fields) {
        Dict retData = Dict.create();
        final T entity = baseDao.getOne(new QueryWrapper<T>().select(fields).allEq(condition));
        if (Objects.isNull(entity)) {
            return retData;
        }
        Dict dict = Dict.parse(entity);
        Arrays.stream(fields).forEach(key -> {
            Object value = dict.getObj(key);
            if (Objects.nonNull(value) && JSONUtil.isJson(value.toString())) {
                value = JSONUtil.toBean(value.toString(), Dict.class);
            }
            retData.set(key, value);
        });
        return retData;
    }

    /**
     * 实现验证注解(返回true表示数据已经存在)
     *
     * @param value                      The value to check for
     * @param fieldName                  The name of the field for which to check if the value exists
     * @param constraintValidatorContext The ValidatorContext
     * @param param                      param
     * @return boolean
     */
    @Override
    public boolean validateExists(Object value, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        String tableName = param.getStr("tableName");
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        return 1 == checkRecordIsExists(tableName, Dict.create().set(fieldName, value));
    }

    /**
     * 验证数据的唯一性 (返回true表示数据已经存在)
     *
     * @param value                      值
     * @param fieldName                  字段名字
     * @param constraintValidatorContext 验证上下文对象
     * @param param                      参数
     * @return boolean
     */
    @Override
    public boolean validateUnique(Object value, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) {
        String tableName = param.getStr("tableName");
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        return checkRecordIsUnique(tableName, Dict.create().set(fieldName, value)) > 1;
    }

    /**
     * 获取分页对象信息
     *
     * @param param 分页参数
     * @return IPage
     */
    @Override
    public IPage<R> constructMyBatisPageObject(Dict param) {
        Dict pageDict = getPageDictFromParam(param);
        return new Page<>(pageDict.getInt("page"), pageDict.getInt("pageSize"));
    }

    /**
     * 获取分页信息
     *
     * @param param 查询参数
     * @return GXPagination
     */
    @Override
    public IPage<R> generatePage(Dict param) {
        String mapperMethodName = "listOrSearchPage";
        return generatePage(param, mapperMethodName);
    }

    /**
     * 分页  返回实体对象
     *
     * @param param            参数
     * @param mapperMethodName Mapper方法
     * @return GXPagination
     */
    @Override
    public IPage<R> generatePage(Dict param, String mapperMethodName) {
        final IPage<R> riPage = constructMyBatisPageObject(param);
        baseDao.generatePage(riPage, param, mapperMethodName);
        return riPage;
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
