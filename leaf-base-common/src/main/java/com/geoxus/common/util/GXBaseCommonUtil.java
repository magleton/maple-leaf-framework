package com.geoxus.common.util;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.geoxus.common.dto.GXBasePagingReqDto;
import com.geoxus.common.dto.protocol.req.GXBaseSearchReqProtocol;

public class GXBaseCommonUtil {
    private GXBaseCommonUtil() {

    }

    /**
     * 将搜索条件转换为指定的目标DTO对象
     *
     * @param searchReqProtocol 搜索条件
     * @param clazz             目标对象类型
     * @param <T>               目标对象类型
     * @return GXBasePagingReqDto
     */
    public static <T extends GXBasePagingReqDto> T searchConvertSpecialTargetObject(GXBaseSearchReqProtocol searchReqProtocol, Class<T> clazz) {
        Dict pagingInfo = searchReqProtocol.getPagingInfo();
        Integer page = pagingInfo.getInt("page");
        Integer pageSize = pagingInfo.getInt("pageSize");
        Dict searchCondition = searchReqProtocol.getSearchCondition();
        Dict condition = Dict.create().set("page", page).set("pageSize", pageSize);
        condition.putAll(searchCondition);
        T targetObject = JSONUtil.toBean(JSONUtil.toJsonStr(condition), clazz);
        GXValidatorUtil.validateEntity(targetObject);
        return targetObject;
    }
}
