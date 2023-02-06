package cn.maple.core.framework.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.*;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.exception.GXBeanValidateException;
import cn.maple.core.framework.exception.GXBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
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
     * @param data   明文数据
     * @param key    加密KEY
     * @param expiry 加密之后的数据过期时间  0 永不过期
     * @return String
     */
    public static String encryptedData(Dict data, String key, int expiry) {
        if (Objects.isNull(data) || data.isEmpty()) {
            throw new GXBusinessException("加密数据明文不能为空");
        }
        if (CharSequenceUtil.isEmpty(key)) {
            throw new GXBusinessException("加密KEY不能为空");
        }
        key = CharSequenceUtil.format("{}{}", key, GXCommonConstant.COMMON_ENCRYPT_KEY);
        return GXAuthCodeUtils.authCodeEncode(JSONUtil.toJsonStr(data), key, expiry);
    }

    /**
     * 解密手机号码
     *
     * @param encryptedStr 加密字符串
     * @param key          解密KEY
     * @return String
     */
    public static Dict decryptedData(String encryptedStr, String key) {
        if (CharSequenceUtil.isEmpty(key)) {
            throw new GXBusinessException("解密KEY不能为空");
        }
        key = CharSequenceUtil.format("{}{}", key, GXCommonConstant.COMMON_ENCRYPT_KEY);
        final String s = GXAuthCodeUtils.authCodeDecode(encryptedStr, key);
        if ("{}".equals(s)) {
            return Dict.create();
        }
        return JSONUtil.toBean(s, Dict.class);
    }

    /**
     * 转换Map.toString()到Map
     *
     * @param mapString map的String表示
     * @return Map
     */
    public static Map<String, Object> convertStrToMap(String mapString) {
        return Arrays.stream(mapString.replace("{", "").replace("}", "").split(",")).map(arrayData -> arrayData.split("=")).collect(Collectors.toMap(d -> d[0].trim(), d -> d[1]));
    }

    /**
     * 将字符串转化为一个Dict
     * eg:
     * convertStrToDict("type='news',phone='13800138000'")
     * OR
     * convertStrToDict("{\"type\":\"news\" , \"phone\":\"13800138000\"}")
     *
     * @param str 字符串
     * @return Dict
     */
    public static Dict convertStrToDict(String str) {
        if (CharSequenceUtil.isBlank(str)) {
            return Dict.create();
        }
        if (JSONUtil.isTypeJSON(str)) {
            return JSONUtil.toBean(str, Dict.class);
        }
        return Convert.convert(new cn.hutool.core.lang.TypeReference<>() {
        }, convertStrToMap(str));
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
     * @param copyOptions 复制选项  可以设置自定义的TypeConvert来自定义转换规则
     * @param extraData   额外参数
     * @return 目标对象
     */
    public static <S, T> T convertSourceToTarget(S source, Class<T> tClass, String methodName, CopyOptions copyOptions, Object extraData) {
        if (Objects.isNull(source)) {
            return null;
        }
        if (CharSequenceUtil.isBlank(methodName)) {
            methodName = GXCommonConstant.DEFAULT_CUSTOMER_PROCESS_METHOD_NAME;
        }
        if (Objects.isNull(extraData)) {
            extraData = Dict.create();
        }
        try {
            copyOptions = ObjectUtil.defaultIfNull(copyOptions, GXCommonUtils::getDefaultCopyOptions);
            T target = ReflectUtil.newInstanceIfPossible(tClass);
            BeanUtil.copyProperties(source, target, copyOptions);
            if (CharSequenceUtil.isNotEmpty(methodName)) {
                reflectCallObjectMethod(target, methodName, extraData);
            }
            reflectCallObjectMethod(target, "verify");
            return target;
        } catch (Exception e) {
            Throwable cause = Optional.ofNullable(e.getCause().getCause()).orElse(e.getCause());
            throw new GXBusinessException(cause.getMessage(), cause);
        }
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
        return convertSourceToTarget(source, tClass, methodName, copyOptions, Dict.create());
    }

    /**
     * 将任意对象转换为指定类型的对象
     *
     * @param collection 需要转换的对象列表
     * @param tClass     目标对象的类型
     * @return List
     */
    public static <R> List<R> convertSourceListToTargetList(Collection<?> collection, Class<R> tClass) {
        return convertSourceListToTargetList(collection, tClass, GXCommonConstant.DEFAULT_CUSTOMER_PROCESS_METHOD_NAME, null);
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
    public static <R> List<R> convertSourceListToTargetList(Collection<?> collection, Class<R> tClass, String methodName, CopyOptions copyOptions) {
        return convertSourceListToTargetList(collection, tClass, methodName, copyOptions, Dict.create());
    }

    /**
     * 将任意对象转换为指定类型的对象
     *
     * @param collection  需要转换的对象列表
     * @param tClass      目标对象的类型
     * @param methodName  需要条用的方法名字
     * @param copyOptions 需要拷贝的选项
     * @param extraData   额外参数
     * @return List
     */
    public static <R> List<R> convertSourceListToTargetList(Collection<?> collection, Class<R> tClass, String methodName, CopyOptions copyOptions, Object extraData) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.emptyList();
        }
        return collection.stream().map(source -> convertSourceToTarget(source, tClass, methodName, copyOptions, extraData)).collect(Collectors.toList());
    }

    /**
     * 动态调用对象中的方法
     *
     * @param serviceClass 目标对象类型 必须存在于Spring Bean容器中
     * @param methodName   对象中的方法
     * @param params       参数
     */
    public static Object reflectCallObjectMethod(Class<?> serviceClass, String methodName, Object... params) {
        Object target = GXSpringContextUtils.getBean(serviceClass);
        return reflectCallObjectMethod(target, methodName, params);
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
        if (Objects.isNull(object)) {
            LOG.warn("反射调用的object对象为null");
            return null;
        }
        if (CharSequenceUtil.isEmpty(methodName)) {
            methodName = GXCommonConstant.DEFAULT_CUSTOMER_PROCESS_METHOD_NAME;
        }
        if (Objects.isNull(params)) {
            params = new Object[0];
        }
        Class<?>[] classes = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            if (Objects.nonNull(params[i])) {
                classes[i] = params[i].getClass();
            }
        }
        Method method = ReflectUtil.getMethod(object.getClass(), methodName, classes);
        if (Objects.isNull(method) && classes.length == 0) {
            method = ReflectUtil.getMethodByName(object.getClass(), methodName);
        }
        if (Objects.isNull(method)) {
            LOG.warn("方法{}.{}({})不存在,反射调用失败!", object.getClass().getSimpleName(), methodName, params);
            return null;
        }
        Object retVal;
        try {
            retVal = ReflectUtil.invoke(object, method, params);
        } catch (UtilException e) {
            Throwable cause = e.getCause();
            Throwable targetException = ((InvocationTargetException) cause).getTargetException();
            if (targetException instanceof GXBeanValidateException) {
                throw (GXBeanValidateException) targetException;
            }
            String exceptionMessage = CharSequenceUtil.isEmpty(targetException.getMessage()) ? "系统反射调用失败" : targetException.getMessage();
            LOG.error("系统反射调用{}.{}({})失败 , [错误消息 : {}] [错误原因 : {}]", object.getClass().getSimpleName(), methodName, params, e.getMessage(), cause);
            throw new GXBusinessException(exceptionMessage, targetException);
        }
        return retVal;
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
     * 构建菜单树
     *
     * @param sourceList      源列表
     * @param rootParentValue 根父级的值, 一般是 0
     * @param <R>             返回的数据类型
     * @return 列表
     */
    public static <R> List<R> buildDeptTree(List<R> sourceList, Object rootParentValue) {
        String methodName = "getParentId";
        // JDK8的stream处理, 把根分类区分出来
        List<R> roots = sourceList.stream().filter(obj -> {
            Object parentId = GXCommonUtils.reflectCallObjectMethod(obj, methodName);
            return Objects.equals(parentId, rootParentValue);
        }).collect(Collectors.toList());
        // 把非根分类区分出来
        List<R> subs = sourceList.stream().filter(obj -> {
            Object parentId = GXCommonUtils.reflectCallObjectMethod(obj, methodName);
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

    /**
     * 解码服务链接信息
     * <pre>
     *     1、数据库
     *     2、redis
     *     3、MongoDB
     * </pre>
     *
     * @param connectEncodeStr 加密的链接信息
     * @return 解码之后的链接信息
     */
    public static <R> R decodeConnectStr(String connectEncodeStr, Class<R> targetClazz) {
        String secretKey = System.getProperty(GXCommonConstant.DATA_SOURCE_SECRET_KEY);
        if (CharSequenceUtil.isEmpty(secretKey)) {
            secretKey = System.getenv(GXCommonConstant.DATA_SOURCE_SECRET_KEY_ENV);
        }
        if (CharSequenceUtil.isEmpty(secretKey)) {
            GXLoggerUtils.logDebug(LOG, "解密密钥为空, 连接信息不进行解密操作");
            return Convert.convert(targetClazz, connectEncodeStr);
        }
        if (!Base64.isBase64(connectEncodeStr)) {
            return Convert.convert(targetClazz, connectEncodeStr);
        }
        String s = GXAuthCodeUtils.authCodeDecode(connectEncodeStr, secretKey);
        if (CharSequenceUtil.equalsIgnoreCase(s, "{}")) {
            GXLoggerUtils.logDebug(LOG, "链接信息参数解码失败, 将使用原始的链接信息");
            return Convert.convert(targetClazz, connectEncodeStr);
        }
        s = HtmlUtil.unescape(s);
        return Convert.convert(targetClazz, s);
    }

    /**
     * 获取默认的CopyOptions
     *
     * @return CopyOptions
     */
    public static CopyOptions getDefaultCopyOptions() {
        CopyOptions copyOptions = CopyOptions.create();
        copyOptions.setConverter((type, value) -> {
            if (Objects.nonNull(value) && value.getClass().isAssignableFrom(String.class) && JSONUtil.isTypeJSON(value.toString())) {
                return JSONUtil.parse(value);
            }
            return Convert.convertWithCheck(type, value, null, false);
        });
        return copyOptions;
    }

    /**
     * 获取指定类上的泛型Class
     *
     * @param clazz 目标Class
     * @param index 泛型的索引
     * @return Class
     */
    @SuppressWarnings("all")
    public static <R> Class<R> getGenericClassType(Class<?> clazz, Integer index) {
        return (Class<R>) TypeUtil.getClass(((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[index]);
    }
}
