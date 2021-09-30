package com.geoxus.core.framework.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geoxus.common.dto.protocol.req.GXBaseSearchReqProtocol;
import com.geoxus.common.dto.protocol.res.GXPaginationProtocol;
import com.geoxus.common.exception.GXBusinessException;
import com.geoxus.common.pojo.GXBusinessStatusCode;
import com.geoxus.common.util.GXBaseCommonUtil;
import com.geoxus.core.framework.mapper.GXFrameworkBaseMapper;
import com.geoxus.core.framework.service.GXBusinessService;
import com.geoxus.core.constant.GXBaseBuilderConstant;
import com.geoxus.common.constant.GXCommonConstant;
import com.geoxus.core.dao.GXBaseDao;
import com.geoxus.core.framework.validator.GXValidateDBExistsService;
import com.geoxus.core.framework.validator.GXValidateDBUniqueService;

import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class GXBusinessServiceImpl<T, M extends GXFrameworkBaseMapper<T>, D extends GXBaseDao<M, T>> extends GXBaseServiceImpl<T, M, D> implements GXBusinessService<T, M, D>, GXValidateDBExistsService, GXValidateDBUniqueService {
    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    @Override
    public <R> GXPaginationProtocol<R> listOrSearchPage(GXBaseSearchReqProtocol searchReqDto) {
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
    public <R> GXPaginationProtocol<R> listOrSearchPage(Dict param) {
        return generatePage(param);
    }

    /**
     * 内容详情
     *
     * @param param 参数
     * @return Dict
     */
    @Override
    public Dict detail(Dict param) {
        final String tableName = (String) param.remove("tableName");
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXBusinessException("请提供表名!");
        }
        final String fields = (String) Optional.ofNullable(param.remove("fields")).orElse("*");
        final boolean remove = (boolean) Optional.ofNullable(param.remove("remove")).orElse(false);
        Set<String> lastFields = Arrays.stream(CharSequenceUtil.replace(fields, " ", "").split(",")).collect(Collectors.toSet());
        Dict condition = Convert.convert(Dict.class, Optional.ofNullable(param.getObj(GXBaseBuilderConstant.SEARCH_CONDITION_NAME)).orElse(Dict.create()));
        return getFieldValueBySQL(tableName, lastFields, condition, remove);
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
     * @param param 参数
     * @return IPage
     */
    @Override
    public <R> IPage<R> constructPageObjectFromParam(Dict param) {
        final Dict pageInfo = getPageInfoFromParam(param);
        return new Page<>(pageInfo.getInt("page"), pageInfo.getInt("pageSize"));
    }

    /**
     * 从请求参数中获取分页的信息
     *
     * @param param 参数
     * @return Dict
     */
    @Override
    public Dict getPageInfoFromParam(Dict param) {
        int currentPage = GXCommonConstant.DEFAULT_CURRENT_PAGE;
        int pageSize = GXCommonConstant.DEFAULT_PAGE_SIZE;
        final Dict pagingInfo = Convert.convert(Dict.class, param.getObj("pagingInfo"));
        if (null != pagingInfo) {
            if (null != pagingInfo.getInt("page")) {
                currentPage = pagingInfo.getInt("page");
            }
            if (null != pagingInfo.getInt("pageSize")) {
                pageSize = pagingInfo.getInt("pageSize");
            }
        }
        return Dict.create().set("page", currentPage).set("pageSize", pageSize);
    }

    /**
     * 获取分页信息
     *
     * @param param 查询参数
     * @return GXPagination
     */
    @Override
    public <R> GXPaginationProtocol<R> generatePage(Dict param) {
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
    public <R> GXPaginationProtocol<R> generatePage(Dict param, String mapperMethodName) {
        final IPage<R> riPage = constructPageObjectFromParam(param);
        Method mapperMethod = ReflectUtil.getMethodByName(baseMapper.getClass(), mapperMethodName);
        if (Objects.isNull(mapperMethod)) {
            Class<?>[] interfaces = baseMapper.getClass().getInterfaces();
            if (interfaces.length > 0) {
                String canonicalName = interfaces[0].getCanonicalName();
                throw new GXBusinessException(CharSequenceUtil.format("请在{}类中实现{}方法", canonicalName, mapperMethodName));
            }
            throw new GXBusinessException(CharSequenceUtil.format("请在相应的Mapper类中实现{}方法", mapperMethodName));
        }
        final List<R> list = ReflectUtil.invoke(baseMapper, mapperMethod, riPage, param);
        riPage.setRecords(list);
        return new GXPaginationProtocol<>(riPage.getRecords(), riPage.getTotal(), riPage.getSize(), riPage.getCurrent());
    }

    /**
     * 获取记录的父级path
     *
     * @param parentId   父级ID
     * @param appendSelf 　是否将parentId附加到返回结果上面
     * @return String
     */
    @Override
    public String getParentPath(Class<T> clazz, Long parentId, boolean appendSelf) {
        Dict condition = Dict.create().set(getPrimaryKey(), parentId);
        final Dict dict = getFieldValueBySQL(clazz, CollUtil.newHashSet("path"), condition, false);
        if (null == dict || dict.isEmpty()) {
            return "0";
        }
        if (appendSelf) {
            return CharSequenceUtil.format("{}-{}", dict.getStr("path"), parentId);
        }
        return dict.getStr("path");
    }

    /**
     * 加密手机号码
     *
     * @param phoneNumber 明文手机号
     * @return String
     */
    @Override
    public String encryptedPhoneNumber(String phoneNumber) {
        return GXBaseCommonUtil.encryptedPhoneNumber(phoneNumber);
    }

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @return String
     */
    @Override
    public String decryptedPhoneNumber(String encryptPhoneNumber) {
        return GXBaseCommonUtil.decryptedPhoneNumber(encryptPhoneNumber);
    }

    /**
     * 隐藏手机号码的指定几位为指定的字符
     *
     * @param phoneNumber  手机号码
     * @param startInclude 开始字符位置(包含,从0开始)
     * @param endExclude   结束字符位置
     * @param replacedChar 替换为的字符
     * @return String
     */
    @Override
    public String hiddenPhoneNumber(CharSequence phoneNumber, int startInclude, int endExclude, char replacedChar) {
        return GXBaseCommonUtil.hiddenPhoneNumber(phoneNumber, startInclude, endExclude, replacedChar);
    }
}
