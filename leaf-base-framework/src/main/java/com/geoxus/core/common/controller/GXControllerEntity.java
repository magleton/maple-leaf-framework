package com.geoxus.core.common.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToTargetAnnotation;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.entity.GXBaseEntity;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.validator.group.GXCreateGroup;
import com.geoxus.core.common.validator.group.GXUpdateGroup;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author zj chen <britton@126.com>
 * @version 1.0
 * @since 2020-09-26
 */
public interface GXControllerEntity<T extends GXBaseEntity> {
    /**
     * 创建数据
     */
    default GXResultUtils<?> create(@Valid @GXRequestBodyToTargetAnnotation(groups = {GXCreateGroup.class}) T target) {
        return GXResultUtils.ok(GXCommonConstants.DEFAULT_DATA);
    }

    /**
     * 更新数据
     *
     * @param target
     * @return
     */
    default GXResultUtils<?> update(@Valid @GXRequestBodyToTargetAnnotation(groups = {GXUpdateGroup.class}) T target) {
        return GXResultUtils.ok(GXCommonConstants.DEFAULT_DATA);
    }

    /**
     * 删除数据
     */
    default GXResultUtils<?> delete(@RequestBody Dict param) {
        return GXResultUtils.ok(GXCommonConstants.DEFAULT_DATA);
    }

    /**
     * 列表或者搜索
     */
    default GXResultUtils<?> listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok(GXCommonConstants.DEFAULT_DATA);
    }

    /**
     * 内容详情
     */
    default GXResultUtils<?> detail(@RequestBody Dict param) {
        return GXResultUtils.ok(GXCommonConstants.DEFAULT_DATA);
    }

    /**
     * 从token中获取用户ID
     *
     * @param tokenName   token的名字
     * @param tokenIdName token中的字段表示
     * @return Long
     */
    default long getUserIdFromToken(String tokenName, String tokenIdName) {
        return GXHttpContextUtils.getUserIdFromToken(tokenName, tokenIdName);
    }
}
