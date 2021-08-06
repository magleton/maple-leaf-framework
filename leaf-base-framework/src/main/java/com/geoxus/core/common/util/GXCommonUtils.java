package com.geoxus.core.common.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.*;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.event.GXBaseEvent;
import com.geoxus.core.rpc.config.GXRabbitMQRPCRemoteServersConfig;
import com.geoxus.core.rpc.service.GXRabbitMQRPCClientService;
import com.github.benmanes.caffeine.cache.CacheLoader;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "unused"})
public class GXCommonUtils {
    @GXFieldCommentAnnotation(zhDesc = "日志对象")
    private static final Logger LOG = LoggerFactory.getLogger(GXCommonUtils.class);

    private GXCommonUtils() {
    }

    /**
     * 根据key获取配置文件中的配置信息
     * <pre>
     *     {@code
     *     getEnvironmentValue(" alipay.appId ", String.class)
     *     }
     * </pre>
     *
     * @param key       key
     * @param clazzType 返回类型
     * @param <R>       泛型类型
     * @return <R>
     */
    public static <R> R getEnvironmentValue(String key, Class<R> clazzType) {
        final R envValue = GXSpringContextUtils.getEnvironment().getProperty(key, clazzType);
        if (null == envValue) {
            return getClassDefaultValue(clazzType);
        }
        return envValue;
    }

    /**
     * 根据key获取配置文件中的配置信息
     * <pre>
     *     {@code
     *     getEnvironmentValue(" alipay.appId ", String.class, " ")
     *     }
     *     </pre>
     *
     * @param key          KEY
     * @param clazzType    返回类型
     * @param defaultValue 泛型类型
     * @param <R>          <R>
     * @return <R>
     */
    public static <R> R getEnvironmentValue(String key, Class<R> clazzType, R defaultValue) {
        final R envValue = GXSpringContextUtils.getEnvironment().getProperty(key, clazzType);
        if (null == envValue) {
            return defaultValue;
        }
        return envValue;
    }

    /**
     * 根据key获取配置文件中的配置信息
     *
     * @param key Key
     * @return String
     */
    public static String getEnvironmentValue(String key) {
        final String property = GXSpringContextUtils.getEnvironment().getProperty(key);
        if (null == property) {
            return "";
        }
        return property;
    }

    /**
     * 获取激活的Profile
     *
     * @return String
     */
    public static String getActiveProfile() {
        return GXSpringContextUtils.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * JSON字符串转Dict
     *
     * @param jsonStr JSON字符串
     * @return Dict
     */
    public static Dict jsonConvertDict(String jsonStr) {
        return jsonConvertAnyObject(jsonStr, Dict.class);
    }

    /**
     * JSON转换为List<Dict>
     *
     * @param jsonStr JSON字符串
     * @return List
     */
    public static List<Dict> jsonConvertDictList(String jsonStr) {
        if (!JSONUtil.isJson(jsonStr)) {
            LOG.error("jsonConvertDict : {}", "请传递正确的JSON格式的字符串");
            return Collections.emptyList();
        }
        if (JSONUtil.isJsonArray(jsonStr)) {
            return jsonConvertAnyObject(jsonStr, new TypeReference<List<Dict>>() {
            });
        }
        return Collections.emptyList();
    }

    /**
     * 获取Class的JVM默认值
     *
     * @param clazzType Class 对象
     * @param <R>       R
     * @return R
     */
    public static <R> R getClassDefaultValue(Class<R> clazzType) {
        if (ClassUtil.isBasicType(clazzType) && !ClassUtil.isPrimitiveWrapper(clazzType)) {
            return Convert.convert(clazzType, ClassUtil.getDefaultValue(clazzType));
        }
        return ReflectUtil.newInstanceIfPossible(clazzType);
    }

    /**
     * 获取Class的JVM默认值
     *
     * @param clazzType Class 对象
     * @param <R>       R
     * @return R
     */
    public static <R> R getClassDefaultValue(Type clazzType) {
        Class<?> aClass = TypeUtil.getClass(clazzType);
        Object o = ReflectUtil.newInstanceIfPossible(aClass);
        return Convert.convert(clazzType, o);
    }

    /**
     * 获取Class的JVM默认值
     *
     * @param clazzType Class 对象
     * @param <R>       R
     * @return R
     */
    public static <R> R getClassDefaultValue(TypeReference<R> clazzType) {
        Class<?> aClass = TypeUtil.getClass(clazzType.getType());
        Object o = ReflectUtil.newInstanceIfPossible(aClass);
        return Convert.convert(clazzType.getType(), o);
    }

    /**
     * 获取Class的JVM默认值
     *
     * @param clazzType Class 对象
     * @param <R>       R
     * @return R
     */
    public static <R> R getClassDefaultValue(TypeReference<R> clazzType, R defaultValue) {
        R classDefaultValue = getClassDefaultValue(clazzType);
        if (Objects.isNull(classDefaultValue)) {
            return defaultValue;
        }
        return classDefaultValue;
    }

    /**
     * 通过路径获取对象中的值
     * <pre>{@code
     *     getDataByPath(Dict.create ().set(" aaa ", " bbbb "),"aaa" ,String.class)
     *     }
     * </pre>
     *
     * @param obj       对象
     * @param key       键
     * @param clazzType Class对象
     * @param <R>       R
     * @return R
     */
    public static <R> R getObjectFieldValueByPath(Object obj, String key, Class<R> clazzType) {
        final JSON parse = JSONUtil.parse(obj);
        final R value = parse.getByPath(key, clazzType);
        if (null == value) {
            return getClassDefaultValue(clazzType);
        }
        return value;
    }

    /**
     * 获取一个接口实现的所有接口
     * <pre>{@code
     * getInterfaces(UUserService.class, targetList)
     * }
     * </pre>
     *
     * @param clazz      Class 对象
     * @param targetList 目标Class列表
     */
    public static void getInterfaces(Class<?> clazz, List<Class<?>> targetList) {
        for (Class<?> clz : clazz.getInterfaces()) {
            targetList.add(clz);
            if (clz.getInterfaces().length > 0) {
                getInterfaces(clz, targetList);
            }
        }
    }

    /**
     * 处理Class的字段信息
     *
     * @param clz  Class 对象
     * @param data 数据
     */
    @SuppressWarnings("all")
    public static void clazzFields(Class<?> clz, Dict data) {
        final Field[] fields = ReflectUtil.getFields(clz);
        for (Field field : fields) {
            final GXFieldCommentAnnotation fieldAnnotation = field.getAnnotation(GXFieldCommentAnnotation.class);
            if (null == fieldAnnotation) {
                continue;
            }
            if (fieldAnnotation.isShow()) {
                final String fieldName = field.getName();
                final boolean isShow = fieldAnnotation.isShow();
                final String fieldComment = fieldAnnotation.value();
                final long fieldCode = fieldAnnotation.code();
                data.putIfAbsent(fieldName, Dict.create()
                        .set("code", fieldCode)
                        .set("isShow", isShow)
                        .set("comment", fieldComment)
                );
            }
        }
    }

    /**
     * 获取当前接口的常量字段信息
     *
     * @param clazz Class对象
     * @return Dict
     */
    public static Dict getConstantsFields(Class<?> clazz) {
        final Dict data = Dict.create();
        final ArrayList<Class<?>> clazzInterfaces = new ArrayList<>();
        clazzInterfaces.add(clazz);
        getInterfaces(clazz, clazzInterfaces);
        for (Class<?> clz : clazzInterfaces) {
            clazzFields(clz, data);
        }
        return data;
    }

    /**
     * 派发事件
     *
     * @param event 事件对象
     */
    public static <T> void publishEvent(GXBaseEvent<T> event) {
        GXSpringContextUtils.getApplicationContext().publishEvent(event);
    }

    /**
     * 转换Map.toString()到Map
     *
     * @param mapString map的String表示
     * @return Map
     */
    public static Map<String, Object> convertStrToMap(String mapString) {
        return Arrays.stream(mapString.replace("{", "").replace("}", "").split(","))
                .map(arrayData -> arrayData.split("="))
                .collect(Collectors.toMap(d -> d[0].trim(), d -> d[1]));
    }

    /**
     * 将实体的某个JSON字段转换为一个Dict
     *
     * @param entity    实体对象
     * @param mainField 主字段 (数据库字段)
     * @return Dict
     */
    public static Dict entityJSONFieldToDict(Object entity, String mainField) {
        return Convert.convert(Dict.class, Convert.convert(Dict.class, entity).get(mainField));
    }

    /**
     * 将一个新key放入已经存在的json字符串中
     *
     * @param jsonStr  JSON字符串
     * @param jsonPath JSON路径
     * @param object   Object
     * @param override 是否复写已经存在的值
     * @return JSONObject
     */
    public static JSONObject putDataToJSONStr(String jsonStr, String jsonPath, Object object, boolean override) {
        if (!JSONUtil.isJsonObj(jsonStr)) {
            return new JSONObject();
        }
        final JSONObject jsonObject = new JSONObject(jsonStr);
        if (override) {
            jsonObject.putByPath(jsonPath, object);
        } else {
            jsonObject.putIfAbsent(jsonPath, object);
        }
        return jsonObject;
    }

    /**
     * 将一个新key放入已经存在的json字符串中
     *
     * @param jsonObject JSON对象
     * @param jsonPath   JSON路径
     * @param object     Object
     * @param override   是否复写已经存在的值
     * @return JSONObject
     */
    public static JSONObject putDataToJSONStr(JSONObject jsonObject, String jsonPath, Object object, boolean override) {
        if (override) {
            jsonObject.putByPath(jsonPath, object);
        } else {
            jsonObject.putIfAbsent(jsonPath, object);
        }
        return jsonObject;
    }

    /**
     * 将json字符串转换为任意对象
     *
     * @param jsonStr JSON字符串
     * @param clazz   Class 对象
     * @param <R>     R
     * @return R
     */
    public static <R> R jsonConvertAnyObject(String jsonStr, Class<R> clazz) {
        if (!JSONUtil.isJson(jsonStr)) {
            LOG.error("不合法的JSON字符串 : {}", jsonStr);
            return getClassDefaultValue(clazz);
        }
        final ObjectMapper objectMapper = GXSpringContextUtils.getBean(ObjectMapper.class);
        try {
            assert objectMapper != null;
            return objectMapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
        }
        return getClassDefaultValue(clazz);
    }

    /**
     * 将json字符串转换为任意对象
     *
     * @param jsonStr   JSON字符串
     * @param reference 需要转换的到类型
     * @param <R>       R
     * @return R
     */
    public static <R> R jsonConvertAnyObject(String jsonStr, TypeReference<R> reference) {
        if (!JSONUtil.isJson(jsonStr)) {
            LOG.error("不合法的JSON字符串");
            return getClassDefaultValue(reference);
        }
        final ObjectMapper objectMapper = GXSpringContextUtils.getBean(ObjectMapper.class);
        try {
            assert objectMapper != null;
            return objectMapper.readValue(jsonStr, reference);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
        }
        return getClassDefaultValue(reference);
    }

    /**
     * 获取json字符串中的任意一个key的数据
     * <pre>
     *     {@code
     *     System.out.println(GXCommonUtils.getJSONValueByAnyPath(s, "data.data1.data2.name", String.class));
     *     System.out.println(GXCommonUtils.getJSONValueByAnyPath(s, "data.data1.data2", Dict.class));
     *     System.out.println(GXCommonUtils.getJSONValueByAnyPath(s, "data.data1", Dict.class));
     *     System.out.println(GXCommonUtils.getJSONValueByAnyPath(s, "data.data1.data2.name.kkkk", String.class));
     *     System.out.println(GXCommonUtils.getJSONValueByAnyPath(s, "data", Dict.class));
     *     }
     * </pre>
     *
     * @param jsonStr JSON字符串
     * @param path    路径
     * @param clazz   Class 对象
     * @param <R>     R
     * @return R
     */
    public static <R> R getJSONValueByAnyPath(String jsonStr, String path, Class<R> clazz) {
        if (!JSONUtil.isJson(jsonStr)) {
            LOG.error("不合法的JSON字符串");
            return getClassDefaultValue(clazz);
        }
        final ObjectMapper objectMapper = GXSpringContextUtils.getBean(ObjectMapper.class);
        try {
            assert objectMapper != null;
            Dict data = objectMapper.readValue(jsonStr, Dict.class);
            String[] paths = CharSequenceUtil.split(path, ".");
            final Object firstObj = data.getObj(paths[0]);
            if (null == firstObj) {
                return getClassDefaultValue(clazz);
            }
            if (!(firstObj instanceof Map)) {
                return Convert.convert(clazz, firstObj);
            }
            Dict tempDict = Convert.convert(Dict.class, firstObj);
            int length = paths.length;
            if (length < 2) {
                return Convert.convert(clazz, tempDict);
            }
            for (int i = 1; i < length - 1; i++) {
                final Object obj = tempDict.getObj(paths[i]);
                if (null == obj) {
                    break;
                }
                if (obj instanceof Map) {
                    tempDict = Convert.convert(Dict.class, obj);
                }
            }
            if (!tempDict.isEmpty()) {
                final Object obj = tempDict.getObj(paths[length - 1]);
                if (Objects.nonNull(obj)) {
                    return Convert.convert(clazz, obj);
                }
            }
        } catch (Exception e) {
            LOG.error("从JSON字符串中获取数据出错 , 错误信息 : {}", e.getMessage());
            return getClassDefaultValue(clazz);
        }
        return getClassDefaultValue(clazz);
    }

    /**
     * 获取json字符串中的任意一个key的数据
     * <pre>
     *     {@code
     *      String str = GXCommonUtils.getJSONValueByAnyPath(s, "data.data1.data2.name", new TypeReference<String>() {});
     *      System.out.println(str);
     *     }
     * </pre>
     *
     * @param jsonStr   JSON字符串
     * @param path      路径
     * @param reference 需要转换到的类型
     * @param <R>       泛型类型
     * @return R
     */
    @SuppressWarnings("unchecked")
    public static <R> R getJSONValueByAnyPath(String jsonStr, String path, TypeReference<R> reference) {
        return (R) getJSONValueByAnyPath(jsonStr, path, TypeUtil.getClass(reference.getType()));
    }

    /**
     * 移除JSON中任意路径的值
     *
     * @param jsonStr JSON字符串
     * @param path    路径
     * @param clazz   Type 对象
     * @param <R>     泛型类型
     * @return R
     */
    @SuppressWarnings("all")
    public static <R> R removeJSONStrAnyPath(String jsonStr, String path, Class<R> clazz) {
        final JSONObject parse = JSONUtil.parseObj(jsonStr);
        int index = CharSequenceUtil.indexOf(path, '.');
        if (index != -1) {
            String mainPath = CharSequenceUtil.sub(path, 0, CharSequenceUtil.lastIndexOfIgnoreCase(path, "."));
            String subPath = CharSequenceUtil.sub(path, CharSequenceUtil.lastIndexOfIgnoreCase(path, ".") + 1, path.length());
            final Object o = parse.get(mainPath);
            if (Objects.nonNull(o)) {
                if (JSONUtil.isJsonArray(o.toString()) && NumberUtil.isInteger(subPath)) {
                    final int delIndex = Integer.parseInt(subPath);
                    if (null != parse.getByPath(mainPath, JSONArray.class)) {
                        parse.getByPath(mainPath, JSONArray.class).remove(delIndex);
                    }
                } else if (null != parse.getByPath(mainPath, JSONObject.class)) {
                    final Object remove = parse.getByPath(mainPath, JSONObject.class).remove(subPath);
                    return Convert.convert(clazz, remove);
                }
            }
            return getClassDefaultValue(clazz);
        }
        return Convert.convert(clazz, parse.remove(path));
    }


    /**
     * 移除JSON中任意路径的值
     *
     * @param jsonStr JSON字符串
     * @param path    路径
     */
    public static void removeJSONStrAnyPath(String jsonStr, String path) {
        removeJSONStrAnyPath(jsonStr, path, Object.class);
    }

    /**
     * 移除JSON中任意路径的值
     *
     * @param parse JSON对象
     * @param path  路径
     * @param clazz Type对象
     * @param <R>   泛型
     * @return R
     */
    @SuppressWarnings("all")
    public static <R> R removeJSONObjectAnyPath(JSONObject parse, String path, Class<R> clazz) {
        int index = CharSequenceUtil.indexOf(path, '.');
        if (index != -1) {
            String mainPath = CharSequenceUtil.sub(path, 0, CharSequenceUtil.lastIndexOfIgnoreCase(path, "."));
            String subPath = CharSequenceUtil.sub(path, CharSequenceUtil.lastIndexOfIgnoreCase(path, ".") + 1, path.length());
            final Object o = parse.getByPath(mainPath);
            if (Objects.nonNull(o)) {
                if (JSONUtil.isJsonArray(o.toString()) && NumberUtil.isInteger(subPath)) {
                    final int delIndex = Integer.parseInt(subPath);
                    if (null != parse.getByPath(mainPath, JSONArray.class)) {
                        parse.getByPath(mainPath, JSONArray.class).remove(delIndex);
                    }
                } else if (null != parse.getByPath(mainPath, JSONObject.class)) {
                    final Object remove = parse.getByPath(mainPath, JSONObject.class).remove(subPath);
                    return Convert.convert(clazz, remove);
                }
            }
            return getClassDefaultValue(clazz);
        }
        return Convert.convert(clazz, parse.remove(path));
    }

    /**
     * 移除JSON中任意路径的值
     *
     * @param parse JSON对象
     * @param path  路径
     */
    public static void removeJSONObjectAnyPath(JSONObject parse, String path) {
        removeJSONObjectAnyPath(parse, path, Object.class);
    }

    /**
     * 获取RPC远程服务的名字
     *
     * @param serverName 服务器名字
     * @param key        key
     * @return String
     */
    public static String getRemoteRPCServerValueByKey(String serverName, String key) {
        final GXRabbitMQRPCRemoteServersConfig serverConfigBean = GXSpringContextUtils.getBean(GXRabbitMQRPCRemoteServersConfig.class);
        assert serverConfigBean != null;
        final Map<String, Map<String, Object>> servers = serverConfigBean.getServers();
        final Map<String, Object> server = servers.get(serverName);
        if (null == server) {
            GXRabbitMQRPCClientService.LOG.error("{} 远程RPC服务不存在", serverName);
            return "";
        }
        if (null != server.get(key)) {
            return (String) server.get(key);
        }
        return "";
    }

    /**
     * 获取Spring的Cache实例
     *
     * @param cacheName cache的名字
     * @return Cache
     */
    public static Cache getSpringCache(String cacheName) {
        final CacheManager cacheManager = GXSpringContextUtils.getBean(CacheManager.class);
        assert cacheManager != null;
        return cacheManager.getCache(cacheName);
    }

    /**
     * 获取Caffeine的Cache对象
     *
     * @param configNameKey 缓存名字的KEY【可以是配置文件中配置的key也可以是缓存名字】
     * @return Cache
     */
    public static <K, V> com.github.benmanes.caffeine.cache.Cache<K, V> getCaffeine(String configNameKey) {
        return GXSingletonUtils.getCaffeineCache(configNameKey);
    }

    /**
     * 获取Caffeine的Cache对象
     *
     * @param configNameKey 缓存名字的KEY【可以是配置文件中配置的key也可以是缓存名字】
     * @param cacheLoader   CacheLoader对象
     * @return LoadingCache
     */
    public static <K, V> com.github.benmanes.caffeine.cache.LoadingCache<K, V> getCaffeine(String configNameKey, CacheLoader<K, V> cacheLoader) {
        return GXSingletonUtils.getCaffeineCache(configNameKey, cacheLoader);
    }

    /**
     * 获取Caffeine的Cache对象
     *
     * @param configNameKey 缓存名字的KEY【可以是配置文件中配置的key也可以是缓存名字】
     * @return AsyncCache
     */
    public static <K, V> com.github.benmanes.caffeine.cache.AsyncCache<K, V> getAsyncCaffeine(String configNameKey) {
        return GXSingletonUtils.getAsyncCaffeine(configNameKey);
    }

    /**
     * 获取Caffeine的Cache对象
     *
     * @param configNameKey 缓存名字的KEY【可以是配置文件中配置的key也可以是缓存名字】
     * @param cacheLoader   CacheLoader对象
     * @return AsyncLoadingCache
     */
    public static <K, V> com.github.benmanes.caffeine.cache.AsyncLoadingCache<K, V> getAsyncCaffeine(String configNameKey, CacheLoader<K, V> cacheLoader) {
        return GXSingletonUtils.getAsyncCaffeine(configNameKey, cacheLoader);
    }

    /**
     * 获取RedissonSpringCacheManager
     *
     * @return RedissonSpringCacheManager
     */
    public static RedissonSpringCacheManager getRedissonCacheManager() {
        return GXSingletonUtils.getRedissonSpringCacheManager();
    }

    /**
     * 获取Redisson客户端对象
     *
     * @return RedissonClient
     */
    public static RedissonClient getRedissonClient() {
        return GXSpringContextUtils.getBean(RedissonClient.class);
    }

    /**
     * 向Spring IOC容器中新增单列对象
     *
     * @param beanName        bean的名字
     * @param singletonObject 需要存储的对象
     */
    public static void registerSingleton(String beanName, Object singletonObject) {
        GXSpringContextUtils.registerSingleton(beanName, singletonObject);
    }

    /**
     * 给现有查询条件新增查询条件
     *
     * @param requestParam       请求参数
     * @param key                添加的key
     * @param value              添加的value
     * @param returnRequestParam 是否返回requestParam
     * @return Dict
     */
    public static Dict addSearchCondition(Dict requestParam, String key, Object value, boolean returnRequestParam) {
        final Object obj = requestParam.getObj(GXBaseBuilderConstants.SEARCH_CONDITION_NAME);
        if (null == obj) {
            return requestParam;
        }
        final Dict data = Convert.convert(Dict.class, obj);
        data.set(key, value);
        if (returnRequestParam) {
            requestParam.put(GXBaseBuilderConstants.SEARCH_CONDITION_NAME, data);
            return requestParam;
        }
        return data;
    }

    /**
     * 给现有查询条件新增查询条件
     *
     * @param requestParam       请求参数
     * @param sourceData         需要添加的map
     * @param returnRequestParam 是否返回requestParam
     * @return Dict
     */
    public static Dict addSearchCondition(Dict requestParam, Dict sourceData, boolean returnRequestParam) {
        final Object obj = requestParam.getObj(GXBaseBuilderConstants.SEARCH_CONDITION_NAME);
        if (null == obj) {
            return requestParam;
        }
        final Dict data = Convert.convert(Dict.class, obj);
        data.putAll(sourceData);
        if (returnRequestParam) {
            requestParam.put(GXBaseBuilderConstants.SEARCH_CONDITION_NAME, data);
            return requestParam;
        }
        return data;
    }

    /**
     * 获取实体的表名字
     *
     * @param clazz Class
     * @return String
     */
    public static String getTableName(Class<?> clazz) {
        final TableName annotation = AnnotationUtil.getAnnotation(clazz, TableName.class);
        if (null != annotation) {
            return annotation.value();
        }
        return "";
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
    public static String hiddenPhoneNumber(CharSequence phoneNumber, int startInclude, int endExclude, char replacedChar) {
        if (Validator.isMobile(phoneNumber)) {
            return CharSequenceUtil.replace(phoneNumber, startInclude, endExclude, replacedChar);
        }
        return "";
    }

    /**
     * 加密手机号码
     *
     * @param phoneNumber 手机号明文
     * @return String
     */
    public static String encryptedPhoneNumber(String phoneNumber) {
        final String prefix = GXCommonUtils.getEnvironmentValue("encrypted.phone.prefix", String.class);
        final String suffix = GXCommonUtils.getEnvironmentValue("encrypted.phone.suffix", String.class);
        final String key = prefix + GXCommonConstants.PHONE_ENCRYPT_KEY + suffix;
        return encryptedPhoneNumber(phoneNumber, key);
    }

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @return String
     */
    public static String decryptedPhoneNumber(String encryptPhoneNumber) {
        final String prefix = GXCommonUtils.getEnvironmentValue("encrypted.phone.prefix", String.class);
        final String suffix = GXCommonUtils.getEnvironmentValue("encrypted.phone.suffix", String.class);
        final String key = prefix + GXCommonConstants.PHONE_ENCRYPT_KEY + suffix;
        return decryptedPhoneNumber(encryptPhoneNumber, key);
    }

    /**
     * 加密手机号码
     *
     * @param phoneNumber 手机号明文
     * @param key         加密KEY
     * @return String
     */
    public static String encryptedPhoneNumber(String phoneNumber, String key) {
        return GXAuthCodeUtils.authCodeEncode(phoneNumber, key);
    }

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @param key                解密KEY
     * @return String
     */
    public static String decryptedPhoneNumber(String encryptPhoneNumber, String key) {
        final String s = GXAuthCodeUtils.authCodeDecode(encryptPhoneNumber, key);
        if ("{}".equals(s)) {
            return encryptPhoneNumber;
        }
        return s;
    }

    /**
     * 获取Logger对象
     *
     * @param clazz Class
     * @return Logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 获取指定缓存中的值
     *
     * @param cacheName 缓存名字
     * @param key       缓存key
     * @param clazz     返回值的类型
     * @param <R>       泛型的类型
     * @return R
     */
    public static <R> R getSpringCacheValue(String cacheName, String key, Class<R> clazz) {
        Cache cache = getSpringCache(cacheName);
        return Objects.requireNonNull(cache).get(key, clazz);
    }

    /**
     * 获取指定缓存中的值
     *
     * @param cacheName    缓存名字
     * @param key          缓存key
     * @param clazz        返回值的类型
     * @param <R>          泛型的类型
     * @param defaultValue 默认值
     * @return R
     */
    public static <R> R getSpringCacheValue(String cacheName, String key, Class<R> clazz, R defaultValue) {
        Cache cache = getSpringCache(cacheName);
        if (Objects.requireNonNull(cache).get(key) == null) {
            return defaultValue;
        }
        return getSpringCacheValue(cacheName, key, clazz);
    }

    /**
     * 设置缓存的值
     *
     * @param cacheName 缓存的名字
     * @param key       缓存的键
     * @param value     缓存的值
     */
    public static void setSpringCacheValue(String cacheName, String key, Object value) {
        Cache springCache = getSpringCache(cacheName);
        Objects.requireNonNull(springCache).put(key, value);
    }

    /**
     * 验证手机号码
     *
     * @param phone 手机号码
     * @return boolean
     */
    public static boolean checkPhone(String phone) {
        final String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        return !ReUtil.isMatch(regex, phone);
    }

    /**
     * 验证固话号码
     *
     * @param telephone 电话号码
     * @return boolean
     */
    public static boolean checkTelephone(String telephone) {
        String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
        return !ReUtil.isMatch(regex, telephone);
    }

    /**
     * 将任意对象转换成Dict
     *
     * @param obj 需要转换的对象
     * @return Dict
     */
    public static Dict convertAnyObjectToDict(Object obj) {
        try {
            return Convert.convert(new cn.hutool.core.lang.TypeReference<Dict>() {
            }, obj);
        } catch (ConvertException e) {
            LOG.error("转换出错了", e);
        }
        return Dict.create();
    }

    /**
     * 获取当前登录用户的ID
     * {@code
     * 0  : 表示普通用户
     * >0 : 表示管理员
     * }
     *
     * @return Long
     */
    public static long getCurrentSessionUserId() {
        return 0L;
    }

    /**
     * 获取AES对象
     *
     * @param key AES的key
     * @return AES
     */
    public static AES getAES(String key) {
        return SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取AES对象
     *
     * @return AES
     */
    public static AES getAES() {
        String key = getEnvironmentValue("common.sensitive.data.key", String.class);
        if (CharSequenceUtil.isBlank(key)) {
            key = "XhFeV780D2218OBRm0xjcWvv";
        }
        return getAES(key);
    }

    /**
     * 将任意对象转换为指定类型的对象
     *
     * @param obj       需要被转换的对象
     * @param beanClass 目标类型
     * @param <T>       目标类型
     * @return T
     */
    public static <T> T toBean(Object obj, Class<T> beanClass) {
        if (Objects.nonNull(obj) && !obj.getClass().getSimpleName().equalsIgnoreCase("object")) {
            return JSONUtil.toBean(JSONUtil.parseObj(obj), beanClass);
        }
        return null;
    }

    /**
     * 将任意对象转换为指定类型的对象
     *
     * @param obj           需要被转换的对象
     * @param typeReference 目标类型
     * @param <T>           目标类型
     * @return T
     */
    public static <T> T toBean(Object obj, cn.hutool.core.lang.TypeReference<T> typeReference, boolean ignoreError) {
        if (Objects.nonNull(obj) && !obj.getClass().getSimpleName().equalsIgnoreCase("object")) {
            return JSONUtil.toBean(JSONUtil.parseObj(obj), typeReference.getType(), ignoreError);
        }
        return null;
    }

    /**
     * 将任意对象转换为指定类型的对象
     *
     * @param obj      需要被转换的对象
     * @param beanType 目标类型
     * @param <T>      目标类型
     * @return T
     */
    public static <T> T toBean(Object obj, Type beanType, boolean ignoreError) {
        if (Objects.nonNull(obj) && !obj.getClass().getSimpleName().equalsIgnoreCase("object")) {
            return JSONUtil.toBean(JSONUtil.parseObj(obj), beanType, ignoreError);
        }
        return null;
    }

    /**
     * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
     * 例如：hello_world=》helloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String toCamelCase(CharSequence name) {
        return CharSequenceUtil.toCamelCase(name);
    }

    /**
     * 将驼峰式命名的字符串转换为使用符号连接方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
     *
     * @param str    转换前的驼峰式命名的字符串，也可以为符号连接形式
     * @param symbol 连接符
     * @return 转换后符号连接方式命名的字符串
     */
    public static String toSymbolCase(CharSequence str, char symbol) {
        return CharSequenceUtil.toSymbolCase(str, symbol);
    }

    /**
     * 将字符串转化为一个Dict
     * eg:
     * strToDict("type='news',phone='13800138000'")
     * OR
     * strToDict("{\"type\":\"news\" , \"phone\":\"13800138000\"}")
     *
     * @param str 字符串
     * @return
     */
    public static Dict strToDict(String str) {
        if (CharSequenceUtil.isBlank(str)) {
            return Dict.create();
        }
        if (JSONUtil.isJson(str)) {
            return JSONUtil.toBean(str, Dict.class);
        }
        return Convert.convert(new cn.hutool.core.lang.TypeReference<Dict>() {
        }, convertStrToMap(str));
    }
}
