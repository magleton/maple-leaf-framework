package cn.maple.core.framework.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.dto.GXBaseData;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import cn.maple.core.framework.service.GXBusinessService;
import cn.maple.core.framework.util.GXCommonUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GXBusinessServiceImpl implements GXBusinessService {
    /**
     * 将任意对象列表通过转换器转换为指定类型的目标对象列表
     *
     * @param targetList 目标列表
     * @param mapStruct  转换器
     * @return 源对象列表
     */
    @Override
    public <S extends GXBaseData, T extends GXBaseData> List<S> convertTargetListToSourceList(List<T> targetList, GXBaseMapStruct<S, T> mapStruct) {
        return GXCommonUtils.convertTargetListToSourceList(targetList, mapStruct);
    }

    /**
     * 将任意目标对象通过转换器转换为指定类型的目标对象
     *
     * @param target    目标对象
     * @param mapStruct 转换器
     * @return 源对象
     */
    @Override
    public <S extends GXBaseData, T extends GXBaseData> S convertTargetToSource(T target, GXBaseMapStruct<S, T> mapStruct) {
        return GXCommonUtils.convertTargetToSource(target, mapStruct);
    }

    /**
     * 将任意的源对象列表通过转换器转换为指定类型的目标对象列表
     *
     * @param sourceList 源列表
     * @param mapStruct  转换器
     * @return 目标对象列表
     */
    @Override
    public <S extends GXBaseData, T extends GXBaseData> List<T> convertSourceListToTargetList(List<S> sourceList, GXBaseMapStruct<S, T> mapStruct) {
        return GXCommonUtils.convertSourceListToTargetList(sourceList, mapStruct);
    }

    /**
     * 将任意源对象通过转换器转换为目标对象
     *
     * @param source    源对象
     * @param mapStruct 转换器
     * @return 目标对象
     */
    @Override
    public <S extends GXBaseData, T extends GXBaseData> T convertSourceToTarget(S source, GXBaseMapStruct<S, T> mapStruct) {
        return GXCommonUtils.convertSourceToTarget(source, mapStruct);
    }

    /**
     * 加密手机号码
     *
     * @param phoneNumber 明文手机号
     * @param key         加密key
     * @return String
     */
    @Override
    public String encryptedPhoneNumber(String phoneNumber, String key) {
        if (CharSequenceUtil.isEmpty(key)) {
            throw new GXBusinessException("手机号加密key不能为空");
        }
        Dict data = Dict.create().set("phone", phoneNumber);
        return GXCommonUtils.encryptedData(data, key, 0);
    }

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @param key                解密key
     * @return String
     */
    @Override
    public String decryptedPhoneNumber(String encryptPhoneNumber, String key) {
        if (CharSequenceUtil.isEmpty(key)) {
            throw new GXBusinessException("手机号解密key不能为空");
        }
        Dict data = GXCommonUtils.decryptedData(encryptPhoneNumber, key);
        return data.getStr("phone");
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
        return GXCommonUtils.hiddenPhoneNumber(phoneNumber, startInclude, endExclude, replacedChar);
    }

    /**
     * 获取实体中指定指定的值
     * <pre>
     *     {@code
     *     getSingleJSONFieldValueByEntity(
     *       GoodsEntity,
     *       "ext.name",
     *       Integer.class
     *       )
     *     }
     * </pre>
     *
     * @param entity 实体对象
     * @param path   路径
     * @return R
     */
    @Override
    public <T, R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type) {
        return getSingleFieldValueByEntity(entity, path, type, GXCommonUtils.getClassDefaultValue(type));
    }

    /**
     * 获取实体中指定指定的值
     * <pre>
     *     {@code
     *     getSingleJSONFieldValueByEntity(
     *       GoodsEntity,
     *       "ext.name",
     *       Integer.class
     *       )
     *     }
     * </pre>
     *
     * @param entity       实体对象
     * @param path         路径
     * @param defaultValue 默认值
     * @return R
     */
    @Override
    public <T, R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, R defaultValue) {
        JSON json = JSONUtil.parse(JSONUtil.toJsonStr(entity));
        int index = CharSequenceUtil.indexOfIgnoreCase(path, "::");
        if (index == -1) {
            if (null == json.getByPath(path)) {
                return defaultValue;
            }
            if (JSONUtil.isTypeJSON(json.getByPath(path).toString())) {
                Dict data = Dict.create();
                Dict dict = JSONUtil.toBean(json.getByPath(path).toString(), Dict.class);
                if (!dict.isEmpty()) {
                    for (Map.Entry<String, Object> entry : dict.entrySet()) {
                        data.set(entry.getKey(), entry.getValue());
                    }
                }
                return Convert.convert(type, data);
            }
            return Convert.convert(type, json.getByPath(path));
        }
        String mainField = CharSequenceUtil.sub(path, 0, index);
        if (null == json.getByPath(mainField)) {
            throw new GXBusinessException(CharSequenceUtil.format("实体的主字段{}不存在!", mainField));
        }
        String subField = CharSequenceUtil.sub(path, index + 2, path.length());
        JSON parse = JSONUtil.parse(json.getByPath(mainField));
        if (null == parse) {
            return defaultValue;
        }
        return Convert.convert(type, parse.getByPath(subField), defaultValue);
    }

    /**
     * 将任意对象转换为指定类型的对象
     * <p>
     * {@code}
     * eg:
     * Dict source = Dict.create().set("username","britton").set("realName","枫叶思源");
     * convertSourceToTarget( source , PersonResDto.class, "customerProcess" , null);
     * OR
     * PersonReqProtocol req = new PersonReqProtocol();
     * req.setUsername("britton");
     * req.setRealName("枫叶思源")；
     * convertSourceToTarget(req ,  PersonResDto.class, "customerProcess" , null);
     * {code}
     *
     * @param source      源对象
     * @param tClass      目标对象类型
     * @param methodName  需要条用的方法名字
     * @param copyOptions 复制选项
     * @return 目标对象
     */
    @Override
    public <S, T> T convertSourceToTarget(S source, Class<T> tClass, String methodName, CopyOptions copyOptions) {
        return GXCommonUtils.convertSourceToTarget(source, tClass, methodName, copyOptions);
    }

    /**
     * 将任意对象转换为指定类型的对象
     *
     * @param collection  需要转换的对象列表
     * @param tClass      目标对象的类型
     * @param methodName  需要条用的方法名字
     * @param copyOptions 需要拷贝的选项
     * @return List
     */
    @Override
    public <R> List<R> convertSourceListToTargetList(Collection<?> collection, Class<R> tClass, String methodName, CopyOptions copyOptions) {
        return GXCommonUtils.convertSourceListToTargetList(collection, tClass, methodName, copyOptions);
    }
}
