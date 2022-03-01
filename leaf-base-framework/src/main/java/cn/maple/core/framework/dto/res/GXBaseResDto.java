package cn.maple.core.framework.dto.res;

import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.dto.GXBaseDto;
import cn.maple.core.framework.util.GXCommonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseResDto extends GXBaseDto {
    /**
     * 将JSON数组转换为指定的对象列表
     *
     * @param jsonArray   JSON数组
     * @param targetClass 目标对象
     * @return 列表
     */
    protected <E> List<E> convertJsonArrayToTarget(String jsonArray, Class<E> targetClass) {
        if (JSONUtil.isJsonArray(jsonArray)) {
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
        if (JSONUtil.isJsonObj(jsonObject)) {
            return JSONUtil.toBean(jsonObject, targetClass);
        }
        return GXCommonUtils.getClassDefaultValue(targetClass);
    }
}
