package cn.maple.core.framework.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.service.GXBusinessService;
import cn.maple.core.framework.util.GXCommonUtils;
import org.springframework.cache.Cache;

import java.util.*;

public class GXBusinessServiceImpl implements GXBusinessService {
    /**
     * 加密手机号码
     *
     * @param phoneNumber 明文手机号
     * @return String
     */
    @Override
    public String encryptedPhoneNumber(String phoneNumber) {
        return GXCommonUtils.encryptedPhoneNumber(phoneNumber);
    }

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @return String
     */
    @Override
    public String decryptedPhoneNumber(String encryptPhoneNumber) {
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
    @Override
    public String hiddenPhoneNumber(CharSequence phoneNumber, int startInclude, int endExclude, char replacedChar) {
        return GXCommonUtils.hiddenPhoneNumber(phoneNumber, startInclude, endExclude, replacedChar);
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
     * 派发事件 (异步事件可以通过在监听器上面添加@Async注解实现)
     *
     * @param event ApplicationEvent对象
     */
    @Override
    public <R> void publishEvent(GXBaseEvent<R> event) {
        GXCommonUtils.publishEvent(event);
    }

    /**
     * 获取当前接口的常量字段信息
     *
     * @return Dict
     */
    @SuppressWarnings("unused")
    @Override
    public Dict getConstantsFields() {
        final Dict data = Dict.create();
        final List<Class<?>> clazzInterfaces = new ArrayList<>();
        GXCommonUtils.getInterfaces(getClass(), clazzInterfaces);
        for (Class<?> clz : clazzInterfaces) {
            GXCommonUtils.clazzFields(clz, data);
        }
        return data;
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
    public <T, R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, int coreModelId) {
        return getSingleFieldValueByEntity(entity, path, type, GXCommonUtils.getClassDefaultValue(type), coreModelId);
    }

    /**
     * 获取实体中指定指定的值
     * <pre>
     *     {@code
     *     getSingleJSONFieldValueByEntity(
     *       GoodsEntity,
     *       "ext.name",
     *       Integer.class
     *       0
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
    public <T, R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, R defaultValue, int coreModelId) {
        JSON json = JSONUtil.parse(JSONUtil.toJsonStr(entity));
        int index = CharSequenceUtil.indexOfIgnoreCase(path, "::");
        if (index == -1) {
            if (null == json.getByPath(path)) {
                return defaultValue;
            }
            if (JSONUtil.isJson(json.getByPath(path).toString())) {
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
     * 获取实体的多个JSON值
     *
     * @param entity 实体对象
     * @param dict   需要获取的数据
     * @return Dict
     */
    @Override
    public <T> Dict getMultiFieldsValueByEntity(T entity, Dict dict, int coreModelId) {
        final Set<String> keySet = dict.keySet();
        final Dict data = Dict.create();
        for (String key : keySet) {
            final Object value = getSingleFieldValueByEntity(entity, key, (Class<?>) dict.getObj(key), coreModelId);
            if (Objects.isNull(value)) {
                continue;
            }
            final String[] strings = CharSequenceUtil.splitToArray(key, "::");
            Object o = data.get(strings[0]);
            if (strings.length > 1) {
                if (null != o) {
                    Dict convert = Convert.convert(Dict.class, o);
                    convert.set(strings[strings.length - 1], value);
                    data.set(strings[0], convert);
                } else {
                    data.set(strings[0], Dict.create().set(strings[strings.length - 1], value));
                }
            } else {
                data.set(strings[strings.length - 1], value);
            }
        }
        return data;
    }

    /**
     * 获取Spring Cache对象
     *
     * @param cacheName 缓存名字
     * @return Cache
     */
    @Override
    public Cache getSpringCache(String cacheName) {
        return GXCommonUtils.getSpringCache(cacheName);
    }

    /**
     * 处理相同前缀的Dict
     *
     * @param dict 要处理的Dict
     * @return Dict
     */
    @Override
    public Dict handleSamePrefixDict(Dict dict) {
        String fieldSeparator = "::";
        final Set<Map.Entry<String, Object>> entries = dict.entrySet();
        final Dict retDict = Dict.create();
        for (Map.Entry<String, Object> entry : entries) {
            final String key = entry.getKey();
            final Object object = entry.getValue();
            if (CharSequenceUtil.contains(key, fieldSeparator)) {
                String[] keys = CharSequenceUtil.splitToArray(key, fieldSeparator);
                Dict data = Convert.convert(Dict.class, Optional.ofNullable(retDict.getObj(keys[0])).orElse(Dict.create()));
                retDict.set(CharSequenceUtil.toCamelCase(keys[0]), data.set(CharSequenceUtil.toCamelCase(keys[1]), object));
            } else {
                retDict.set(CharSequenceUtil.toCamelCase(key), object);
            }
        }
        return retDict;
    }
}
