package cn.maple.core.framework.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.exceptions.UtilException;
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
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.event.GXBaseEvent;
import cn.maple.core.framework.exception.GXBeanValidateException;
import cn.maple.core.framework.exception.GXBusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class GXCommonUtils {
    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(GXCommonUtils.class);

    private GXCommonUtils() {
    }

    /**
     * 根据key获取配置文件中的配置信息
     * <pre>
     *     {@code
     *     getEnvironmentValue("alipay.appId", String.class)
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
     *     getEnvironmentValue("alipay.appId", String.class, " ")
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
     * 获取Dict中指定的key的值
     *
     * @param param 目标dict
     * @param key   需要获取的key名字
     * @param clazz 返回的数据类型
     * @return R
     */
    public static <R> R getValueFromDict(Dict param, String key, Class<R> clazz) {
        Object obj = param.getObj(key);
        if (Objects.nonNull(obj)) {
            return Convert.convert(clazz, obj);
        }
        return null;
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
     * 通过路径获取对象中的值
     * <pre>{@code
     *     getDataByPath(Dict.create().set("aaa", "bbbb"),"aaa" ,String.class)
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
     * 派发事件
     *
     * @param event 事件对象
     */
    public static <T> void publishEvent(GXBaseEvent<T> event) {
        GXSpringContextUtils.getApplicationContext().publishEvent(event);
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
        final String key = prefix + GXCommonConstant.PHONE_ENCRYPT_KEY + suffix;
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
        final String key = prefix + GXCommonConstant.PHONE_ENCRYPT_KEY + suffix;
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
     * 将一个新key放入已经存在的json字符串中
     *
     * @param jsonStr  JSON字符串
     * @param jsonPath JSON路径
     * @param object   Object
     * @param override 是否复写已经存在的值
     * @return JSONObject
     */
    public static JSONObject putDataToJsonStr(String jsonStr, String jsonPath, Object object, boolean override) {
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
     * 将json字符串转换为任意对象
     *
     * @param jsonStr JSON字符串
     * @param clazz   Class 对象
     * @param <R>     R
     * @return R
     */
    public static <R> R jsonStrConvertToTarget(String jsonStr, Class<R> clazz) {
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
    public static <R> R getJsonValueByPath(String jsonStr, String path, Class<R> clazz) {
        if (!JSONUtil.isJson(jsonStr)) {
            LOG.error("不合法的JSON字符串");
            return getClassDefaultValue(clazz);
        }
        final ObjectMapper objectMapper = GXSpringContextUtils.getBean(ObjectMapper.class);
        try {
            assert objectMapper != null;
            Dict data = objectMapper.readValue(jsonStr, Dict.class);
            String[] paths = CharSequenceUtil.split(path, ".").toArray(new String[0]);
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
    public static <S, T> T convertSourceToTarget(S source, Class<T> tClass, String methodName, CopyOptions copyOptions) {
        if (Objects.isNull(source)) {
            LOG.info("源对象不能为null");
            return null;
        }
        copyOptions = ObjectUtil.defaultIfNull(copyOptions, CopyOptions.create());
        reflectCallObjectMethod(source, "beforeMapping", copyOptions);
        T target = ReflectUtil.newInstanceIfPossible(tClass);
        reflectCallObjectMethod(target, "beforeMapping", copyOptions);
        BeanUtil.copyProperties(source, target, copyOptions);
        reflectCallObjectMethod(target, "afterMapping", source);
        if (CharSequenceUtil.isNotEmpty(methodName)) {
            reflectCallObjectMethod(target, methodName);
        }
        return target;
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
    @SuppressWarnings("all")
    public static <R> List<R> convertSourceListToTargetList(Collection<?> collection, Class<R> tClass, String methodName, CopyOptions copyOptions) {
        if (CollUtil.isEmpty(collection)) {
            LOG.info("源对象不能为null");
            return Collections.emptyList();
        }
        List<R> rList = collection.stream().map((source) -> convertSourceToTarget(source, tClass, methodName, copyOptions)).collect(Collectors.toList());
        return rList;
    }

    /**
     * 动态调用对象中的方法
     *
     * @param object     对象
     * @param methodName 对象中的方法
     * @param params     参数
     * @param <R>        对象类型
     */
    public static <R> Object reflectCallObjectMethod(R object, String methodName, Object... params) {
        if (CharSequenceUtil.isEmpty(methodName)) {
            methodName = "customizeProcess";
        }
        if (params == null) {
            params = new Object[0];
        }
        Class<?>[] classes = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            classes[i] = params[i].getClass();
        }
        Method method = ReflectUtil.getMethod(object.getClass(), methodName, classes);
        Object retVal = null;
        if (Objects.nonNull(method)) {
            try {
                retVal = ReflectUtil.invoke(object, method, params);
            } catch (UtilException e) {
                Throwable cause = e.getCause();
                if (cause instanceof InvocationTargetException) {
                    Throwable targetException = ((InvocationTargetException) cause).getTargetException();
                    if (targetException instanceof GXBeanValidateException) {
                        throw (GXBeanValidateException) targetException;
                    }
                }
                throw new GXBusinessException(e.getMessage(), cause);
            }
        }
        return retVal;
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
    public static <R> R removeJsonStrAnyPath(String jsonStr, String path, Class<R> clazz) {
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
    public static void removeJsonStrAnyPath(String jsonStr, String path) {
        removeJsonStrAnyPath(jsonStr, path, Object.class);
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
     * @param source 需要转换的源对象
     * @return Dict
     */
    public static Dict convertSourceToDict(Object source) {
        try {
            return Convert.convert(new cn.hutool.core.lang.TypeReference<>() {
            }, source);
        } catch (ConvertException e) {
            LOG.error("转换出错了", e);
        }
        return Dict.create();
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
     * @return Dict
     */
    public static Dict strToDict(String str) {
        if (CharSequenceUtil.isBlank(str)) {
            return Dict.create();
        }
        if (JSONUtil.isJson(str)) {
            return JSONUtil.toBean(str, Dict.class);
        }
        return Convert.convert(new cn.hutool.core.lang.TypeReference<>() {
        }, convertStrToMap(str));
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
     * 构建菜单树
     *
     * @param sourceList      源列表
     * @param rootParentValue 根父级的值, 一般是 0
     * @param <R>             返回的数据类型
     * @return 列表
     */
    public static <R> List<R> buildDeptTree(List<R> sourceList, Object rootParentValue) {
        // JDK8的stream处理, 把根分类区分出来
        List<R> roots = sourceList.stream().filter(obj -> {
            Object parentId = GXCommonUtils.reflectCallObjectMethod(obj, "getParentId");
            return Objects.equals(parentId, rootParentValue);
        }).collect(Collectors.toList());
        // 把非根分类区分出来
        List<R> subs = sourceList.stream().filter(obj -> {
            Object parentId = GXCommonUtils.reflectCallObjectMethod(obj, "getParentId");
            return !Objects.equals(parentId, rootParentValue);
        }).collect(Collectors.toList());
        // 递归构建结构化的分类信息
        roots.forEach(root -> buildSubs(root, subs));
        return roots;
    }

    /**
     * 构建菜单树的子级
     *
     * @param parent 父结点
     * @param subs   子结点
     * @param <R>    元素类型
     */
    private static <R> void buildSubs(R parent, List<R> subs) {
        List<R> children = subs.stream().filter(sub -> {
            Object parentId = GXCommonUtils.reflectCallObjectMethod(sub, "getParentId");
            Object id = GXCommonUtils.reflectCallObjectMethod(parent, "getId");
            return Objects.equals(parentId, id);
        }).collect(Collectors.toList());
        if (!CollUtil.isEmpty(children)) {
            // 有子分类的情况
            GXCommonUtils.reflectCallObjectMethod(parent, "setChildren", children);
            // 再次递归构建
            children.forEach(child -> buildSubs(child, subs));
        }
    }
}
