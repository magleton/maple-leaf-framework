package cn.maple.core.datasource.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.service.GXDBBaseService;
import cn.maple.core.framework.ddd.repository.GXBaseRepository;
import cn.maple.core.framework.dto.inner.req.GXBaseReqDto;
import cn.maple.core.framework.dto.inner.req.GXQueryParamReqDto;
import cn.maple.core.framework.dto.inner.res.GXBaseResDto;
import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 业务基础Service
 *
 * @param <R> 仓库对象类型
 * @param <Q> 请求参数类型
 * @param <S> 响应数据类型
 */
public class GXDBBaseServiceImpl<R extends GXBaseRepository<Q, S>, Q extends GXBaseReqDto, S extends GXBaseResDto>
        extends GXDBCommonServiceImpl
        implements GXDBBaseService<R, Q, S> {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = GXCommonUtils.getLogger(GXDBBaseServiceImpl.class);

    /**
     * 仓库类型
     */
    @Autowired
    @SuppressWarnings("all")
    protected R repository;

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public Integer checkRecordIsExists(String tableName, Dict condition) {
        return repository.checkRecordIsExists(tableName, condition);
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
        return repository.checkRecordIsUnique(tableName, condition);
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
    public boolean updateFieldByCondition(String tableName, Dict data, Dict condition) {
        return false;
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名
     * @param fieldSet  字段集合
     * @param dataList  数据集合
     * @return int
     */
    @SuppressWarnings("all")
    @Override
    public Integer batchInsert(String tableName, Set<String> fieldSet, List<Dict> dataList) {
        return repository.batchInsert(tableName, fieldSet, dataList);
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    @Override
    public GXPaginationResDto<S> paginate(GXQueryParamReqDto searchReqDto) {
        Integer page = searchReqDto.getPage();
        Integer pageSize = searchReqDto.getPageSize();
        Table<String, String, Object> queryCondition = searchReqDto.getQueryCondition();
        if (Objects.isNull(queryCondition)) {
            queryCondition = HashBasedTable.create();
        }
        return repository.paginate(page, pageSize, queryCondition, CollUtil.newHashSet("*"));
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
     * 获取 Primary Key
     *
     * @return String
     */
    @Override
    public String getPrimaryKey() {
        return "id";
    }
}
