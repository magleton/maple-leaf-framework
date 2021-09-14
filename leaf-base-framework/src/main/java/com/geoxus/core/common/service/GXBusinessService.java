package com.geoxus.core.common.service;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.dao.GXBaseDao;
import com.geoxus.dto.protocol.req.GXBaseSearchReqProtocol;
import com.geoxus.dto.protocol.res.GXBaseResProtocol;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.common.validator.GXValidateDBExists;
import com.geoxus.core.common.validator.GXValidateDBUnique;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.framework.service.GXBaseService;

public interface GXBusinessService<T, M extends GXBaseMapper<T>, D extends GXBaseDao<M, T>> extends GXBaseService<T, M, D>, GXValidateDBExists, GXValidateDBUnique {
    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @param clazz        元素类型
     * @return GXPagination
     */
    <R> GXPagination<R> listOrSearchPage(GXBaseSearchReqProtocol searchReqDto, Class<R> clazz);

    /**
     * 列表或者搜索(分页)
     *
     * @param param 参数
     * @return GXPagination
     */
    <R> GXPagination<R> listOrSearchPage(Dict param, Class<R> clazz);

    /**
     * 内容详情
     *
     * @param param 参数
     * @return Dict
     */
    Dict detail(Dict param);

    /**
     * 批量更新status字段
     *
     * @param param 参数
     * @return boolean
     */
    boolean batchUpdateStatus(Dict param);

    /**
     * 通过条件获取配置信息
     *
     * @param condition 条件
     * @return Dict
     */
    Dict getDataByCondition(Dict condition, String... fields);

    /**
     * 获取分页对象信息
     *
     * @param param 参数
     * @return IPage
     */
    <R> IPage<R> constructPageObjectFromParam(Dict param);

    /**
     * 从请求参数中获取分页的信息
     *
     * @param param 参数
     * @return Dict
     */
    Dict getPageInfoFromParam(Dict param);

    /**
     * 获取分页信息
     *
     * @param param 查询参数
     * @return GXPagination
     */
    <R> GXPagination<R> generatePage(Dict param, Class<R> clazz);

    /**
     * 分页  返回实体对象
     *
     * @param param            参数
     * @param mapperMethodName Mapper方法
     * @return GXPagination
     */
    <R> GXPagination<R> generatePage(Dict param, String mapperMethodName, Class<R> clazz);

    /**
     * 获取记录的父级path
     *
     * @param parentId   父级ID
     * @param appendSelf 　是否将parentId附加到返回结果上面
     * @return String
     */
    String getParentPath(Class<T> clazz, Long parentId, boolean appendSelf);

    /**
     * 加密手机号码
     *
     * @param phoneNumber 明文手机号
     * @return String
     */
    String encryptedPhoneNumber(String phoneNumber);

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @return String
     */
    String decryptedPhoneNumber(String encryptPhoneNumber);

    /**
     * 隐藏手机号码的指定几位为指定的字符
     *
     * @param phoneNumber  手机号码
     * @param startInclude 开始字符位置(包含,从0开始)
     * @param endExclude   结束字符位置
     * @param replacedChar 替换为的字符
     * @return String
     */
    String hiddenPhoneNumber(CharSequence phoneNumber, int startInclude, int endExclude, char replacedChar);
}
