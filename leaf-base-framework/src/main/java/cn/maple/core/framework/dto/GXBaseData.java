package cn.maple.core.framework.dto;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.util.GXCommonUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public abstract class GXBaseData implements Serializable {
    /**
     * 调用自定义的方法进行参数的处理
     *
     * @param data 参数
     * @author britton
     */
    protected void customizeProcess(Dict data) {
    }

    /**
     * @author britton
     * 对请求参数进行补充校验
     */
    protected void verify() {
    }

    /**
     * 将JSON数组转换为指定的对象列表
     *
     * @param jsonArray   JSON数组
     * @param targetClass 目标对象
     * @return 列表
     */
    protected <E> List<E> convertJsonArrayToTarget(String jsonArray, Class<E> targetClass) {
        if (JSONUtil.isTypeJSONArray(jsonArray)) {
            return JSONUtil.toList(jsonArray, targetClass);
        }
        return Collections.emptyList();
    }

    /**
     * 将JSON数组转换为指定的对象列表
     *
     * @param jsonObject  JSON对象
     * @param targetClass 目标对象
     * @return 列表
     */
    protected <E> E convertJsonObjectToTarget(String jsonObject, Class<E> targetClass) {
        if (JSONUtil.isTypeJSONObject(jsonObject)) {
            return JSONUtil.toBean(jsonObject, targetClass);
        }
        return GXCommonUtils.getClassDefaultValue(targetClass);
    }
}
