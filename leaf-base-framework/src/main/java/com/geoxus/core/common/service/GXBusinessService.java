package com.geoxus.core.common.service;

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
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.dto.GXBaseSearchReqDto;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.validator.GXValidateDBExists;
import com.geoxus.core.common.validator.GXValidateDBUnique;
import com.geoxus.core.common.vo.common.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.framework.service.GXBaseService;

import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.stream.Collectors;

public interface GXBusinessService<T> extends GXBaseService<T>, GXValidateDBExists, GXValidateDBUnique {
    /**
     * 创建数据
     *
     * @param target 目标实体
     * @param param  额外参数
     * @return long
     */
    default long create(T target, Dict param) {
        return 0;
    }

    /**
     * 更新数据
     *
     * @param target 目标实体
     * @param param  额外参数
     * @return long
     */
    default long update(T target, Dict param) {
        return 0;
    }

    /**
     * 删除数据
     *
     * @param param 参数
     * @return boolean
     */
    default boolean delete(Dict param) {
        return batchSoftDelete(param);
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    default GXPagination<Dict> listOrSearchPage(GXBaseSearchReqDto searchReqDto) {
        final Dict param = Dict.create();
        if (Objects.nonNull(searchReqDto.getPagingInfo())) {
            param.set("pagingInfo", searchReqDto.getPagingInfo());
        }
        if (Objects.nonNull(searchReqDto.getSearchCondition())) {
            param.set(GXBaseBuilderConstants.SEARCH_CONDITION_NAME, searchReqDto.getSearchCondition());
        }
        if (Objects.nonNull(searchReqDto.getRemoveField())) {
            param.set("removeField", searchReqDto.getRemoveField());
        }
        return generatePage(param);
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param param 参数
     * @return GXPagination
     */
    default GXPagination<Dict> listOrSearchPage(Dict param) {
        return generatePage(param);
    }

    /**
     * 列表或者搜索 (不分页)
     *
     * @param searchReqDto 参数
     * @return List
     */
    default <U extends GXBaseSearchReqDto> List<Dict> listOrSearch(U searchReqDto) {
        final Dict param = Dict.create();
        if (Objects.nonNull(searchReqDto.getSearchCondition())) {
            param.set(GXBaseBuilderConstants.SEARCH_CONDITION_NAME, searchReqDto.getSearchCondition());
        }
        if (Objects.nonNull(searchReqDto.getRemoveField())) {
            param.set("removeField", searchReqDto.getRemoveField());
        }
        return listOrSearch(param);
    }

    /**
     * 列表或者搜索 (不分页)
     *
     * @param param 参数
     * @return List
     */
    default List<Dict> listOrSearch(Dict param) {
        final String profile = GXCommonUtils.getActiveProfile();
        if ("prod".equals(profile)) {
            return Collections.emptyList();
        }
        throw new GXException("请实现自定义的listOrSearch方法");
    }

    /**
     * 内容详情
     *
     * @param param 参数
     * @return Dict
     */
    default Dict detail(Dict param) {
        final String tableName = (String) param.remove("tableName");
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXException("请提供表名!");
        }
        final String fields = (String) Optional.ofNullable(param.remove("fields")).orElse("*");
        final boolean remove = (boolean) Optional.ofNullable(param.remove("remove")).orElse(false);
        Set<String> lastFields = Arrays.stream(CharSequenceUtil.replace(fields, " ", "").split(",")).collect(Collectors.toSet());
        Dict condition = Convert.convert(Dict.class, Optional.ofNullable(param.getObj(CharSequenceUtil.toCamelCase(GXBaseBuilderConstants.SEARCH_CONDITION_NAME))).orElse(Dict.create()));
        return getFieldValueBySQL(tableName, lastFields, condition, remove);
    }

    /**
     * 批量删除
     *
     * @param param 参数
     * @return boolean
     */
    default boolean batchSoftDelete(Dict param) {
        final List<Long> ids = Convert.convert(new TypeReference<List<Long>>() {
        }, param.getObj(getPrimaryKey()));
        if (null == ids) {
            return false;
        }
        final List<T> updateEntities = new ArrayList<>();
        for (Long id : ids) {
            T entity = getById(id);
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
        return updateEntities.isEmpty() || updateBatchById(updateEntities);
    }

    /**
     * 通过条件获取配置信息
     *
     * @param condition 条件
     * @return Dict
     */
    default Dict getDataByCondition(Dict condition, String... fields) {
        Dict retData = Dict.create();
        final T entity = getOne(new QueryWrapper<T>().select(fields).allEq(condition));
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
    default boolean validateExists(Object value, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        String tableName = param.getStr("tableName");
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
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
    default boolean validateUnique(Object value, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) {
        String tableName = param.getStr("tableName");
        if (CharSequenceUtil.isBlank(tableName)) {
            throw new GXException(CharSequenceUtil.format("请指定数据库表的名字 , 验证的字段 {} , 验证的值 : {}", fieldName, value));
        }
        return checkRecordIsUnique(tableName, Dict.create().set(fieldName, value)) > 1;
    }

    /**
     * 获取分页对象信息
     *
     * @param param 参数
     * @return IPage
     */
    default IPage<Dict> constructPageObjectFromParam(Dict param) {
        final Dict pageInfo = getPageInfoFromParam(param);
        return new Page<>(pageInfo.getInt("page"), pageInfo.getInt("pageSize"));
    }

    /**
     * 从请求参数中获取分页的信息
     *
     * @param param 参数
     * @return Dict
     */
    default Dict getPageInfoFromParam(Dict param) {
        int currentPage = GXCommonConstants.DEFAULT_CURRENT_PAGE;
        int pageSize = GXCommonConstants.DEFAULT_PAGE_SIZE;
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
     * @param param       查询参数
     * @param removeField 需要移除的数据
     * @return GXPagination
     */
    default GXPagination<Dict> generatePage(Dict param, Dict removeField) {
        final IPage<Dict> riPage = constructPageObjectFromParam(param);
        GXBaseMapper<Dict> baseMapper = (GXBaseMapper<Dict>) getBaseMapper();
        final List<Dict> list = baseMapper.listOrSearchPage(riPage, param);
        riPage.setRecords(processingListData(list, removeField));
        return new GXPagination<>(riPage.getRecords(), riPage.getTotal(), riPage.getSize(), riPage.getCurrent());
    }

    /**
     * 获取分页信息
     *
     * @param param 查询参数
     * @return GXPagination
     */
    default GXPagination<Dict> generatePage(Dict param) {
        final Set<String> removeFields = Convert.convert(new TypeReference<Set<String>>() {
        }, Optional.ofNullable(param.getObj("removeField")).orElse(CollUtil.newHashSet()));
        Map<String, Object> removeField = CollUtil.newHashMap();
        for (final String s : removeFields) {
            if (CharSequenceUtil.contains(s, "::")) {
                final String[] strings = CharSequenceUtil.splitToArray(s, "::");
                String mainKey = strings[0];
                String subKey = strings[1];
                final Object o = removeField.get(mainKey);
                if (null != o) {
                    final Dict dict = Convert.convert(Dict.class, o).set(subKey, subKey);
                    removeField.put(mainKey, dict);
                } else {
                    removeField.put(mainKey, Dict.create().set(subKey, subKey));
                }
            } else {
                removeField.put(s, s);
            }
        }

        return generatePage(param, Convert.convert(Dict.class, removeField));
    }

    /**
     * 处理列表数据,主要用于删除指定的字段值
     *
     * @param list        数据列表
     * @param removeField 需要移除的数据
     * @return List
     */
    default List<Dict> processingListData(List<Dict> list, Dict removeField) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        if (null == removeField) {
            removeField = Dict.create();
        }
        final List<Dict> retList = CollUtil.newArrayList();
        for (Dict dict : list) {
            retList.add(handleSamePrefixDict(handleDict(dict, removeField)));
        }
        return retList;
    }

    /**
     * 处理Dict数据，主要用于解密数据，去掉当前用户不能访问的数据
     *
     * @param dict        待处理的Dict
     * @param removeField 需要移除的数据
     * @return Dict
     */
    default Dict handleDict(Dict dict, Dict removeField) {
        final HashSet<String> phoneNumberFields = CollUtil.newHashSet("phone", "mobile");
        final Set<Map.Entry<String, Object>> entries = dict.entrySet();
        final Dict retDict = Dict.create();
        for (Map.Entry<String, Object> entry : entries) {
            final String key = entry.getKey();
            Object value = entry.getValue();
            final Object o = removeField.get(key);
            if (null == o) {
                if (!(value instanceof Dict)) {
                    if (CollUtil.contains(phoneNumberFields, key)) {
                        String str = decryptedPhoneNumber(value.toString());
                        if (!str.equals("{}")) {
                            value = str;
                        }
                    }
                    retDict.set(key, value);
                    continue;
                }
                final Dict convert = Convert.convert(Dict.class, value);
                final Set<Map.Entry<String, Object>> entrySet = Convert.convert(Dict.class, value).entrySet();
                final Dict tmpDict = Dict.create();
                for (Map.Entry<String, Object> en : entrySet) {
                    final String enKey = en.getKey();
                    Object enValue = en.getValue();
                    if (null != convert.get(enKey) && CollUtil.contains(phoneNumberFields, enKey)) {
                        String str = decryptedPhoneNumber(enValue.toString());
                        if (!str.equals("{}")) {
                            enValue = str;
                        }
                    }
                    tmpDict.set(enKey, enValue);
                }
                retDict.set(key, tmpDict);
            } else {
                if (value instanceof Dict) {
                    final Dict removeDict = Convert.convert(Dict.class, o);
                    final Set<Map.Entry<String, Object>> entrySet = Convert.convert(Dict.class, value).entrySet();
                    final Dict tmpDict = Dict.create();
                    for (Map.Entry<String, Object> en : entrySet) {
                        final String enKey = en.getKey();
                        Object enValue = en.getValue();
                        if (null == removeDict.get(enKey)) {
                            if (CollUtil.contains(phoneNumberFields, enKey)) {
                                String str = decryptedPhoneNumber(enValue.toString());
                                if (!str.equals("{}")) {
                                    enValue = str;
                                }
                            }
                            tmpDict.set(enKey, enValue);
                        }
                    }
                    retDict.set(key, tmpDict);
                }
            }
        }
        return retDict;
    }

    /**
     * 将IPage信息转换成Pagination对象
     *
     * @param iPage 分页对象
     * @return GXPagination
     */
    default GXPagination<Dict> generatePage(IPage<Dict> iPage, Dict removeField) {
        final List<Dict> records = processingListData(iPage.getRecords(), removeField);
        return new GXPagination<>(records, iPage.getTotal(), iPage.getSize(), iPage.getCurrent());
    }

    /**
     * 分页  返回实体对象
     *
     * @param param            参数
     * @param mapperMethodName Mapper方法
     * @param removeField      需要移除的字段
     * @return GXPagination
     */
    default GXPagination<Dict> generatePage(Dict param, String mapperMethodName, Dict removeField) {
        final Dict pageParam = getPageInfoFromParam(param);
        final IPage<Dict> iPage = new Page<>(pageParam.getInt("page"), pageParam.getInt("pageSize"));
        final List<Dict> list = ReflectUtil.invoke(getBaseMapper(), mapperMethodName, iPage, param);
        iPage.setRecords(processingListData(list, removeField));
        return new GXPagination<>(iPage.getRecords(), iPage.getTotal(), iPage.getSize(), iPage.getCurrent());
    }

    /**
     * 获取记录的父级path
     *
     * @param parentId   父级ID
     * @param appendSelf 　是否将parentId附加到返回结果上面
     * @return String
     */
    default String getParentPath(Class<T> clazz, Long parentId, boolean appendSelf) {
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
    default String encryptedPhoneNumber(String phoneNumber) {
        return GXCommonUtils.encryptedPhoneNumber(phoneNumber);
    }

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @return String
     */
    default String decryptedPhoneNumber(String encryptPhoneNumber) {
        return GXCommonUtils.decryptedPhoneNumber(encryptPhoneNumber);
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
    default String hiddenPhoneNumber(CharSequence phoneNumber, int startInclude, int endExclude, char replacedChar) {
        return GXCommonUtils.hiddenPhoneNumber(phoneNumber, startInclude, endExclude, replacedChar);
    }
}
