package com.geoxus.core.common.util;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import org.slf4j.Logger;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SpEL工具类
 *
 * @author <email>britton@126.com</email>
 */
public class GXSpELToolUtils {
    /**
     * 日志对象
     */
    private static final Logger LOG = GXCommonUtils.getLogger(GXSpELToolUtils.class);

    /**
     * 目标类中方法不存在的提示信息
     */
    private static final String METHOD_NOT_FOUND_TIPS_TEMPLATE = "目标类{}中没有满足签名为{}({})的方法存在~~~";

    /**
     * 私有函数
     * 防止被外部构造
     */
    private GXSpELToolUtils() {

    }

    /**
     * 计算SpEL的表达式
     * <pre> {@code
     *  Dict data = Dict.create().set("name","jack").set("age",12);
     *  String expression = "#data['name']=='jack' and #data['flags']==true";
     *  calculateSpELExpression(data ,expression ,String.class, "data");
     * } </pre>
     *
     * @param data             数据
     * @param expressionString 表达式  #data['name']=='jack' and #data['flags']==true
     * @param beanClass        返回的数据类型  Dog.class
     * @param dataKey          数据key data1 那么expressionString #data1['name']=='jack' and #data1['flags']==true
     * @return T
     */
    public static <T> T calculateSpELExpression(Dict data, String expressionString, Class<T> beanClass, String dataKey) {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        dataKey = Objects.isNull(dataKey) ? "data" : dataKey;
        context.setVariable(dataKey, data);
        try {
            final Expression expression = parser.parseExpression(expressionString);
            return expression.getValue(context, beanClass);
        } catch (SpelEvaluationException e) {
            LOG.error("SpEL表达式失败 , 表达式 : {} , 异常信息 : {}", expressionString, e.getMessage());
        }
        return GXCommonUtils.getClassDefaultValue(beanClass);
    }

    /**
     * 计算SpEL的表达式
     * <pre> {@code
     *  Dict data = Dict.create().set("name","jack").set("age",12);
     *  String expression = "#data['name']=='jack' and #data['flags']==true";
     *  calculateSpELExpression(data ,expression ,String.class, "data");
     * } </pre>
     *
     * @param data             数据
     * @param expressionString 表达式  #data['name']=='jack' and #data['flags']==true
     * @param beanClass        返回的数据类型  Dog.class
     * @return T
     */
    public static <T> T calculateSpELExpression(Dict data, String expressionString, Class<T> beanClass) {
        return calculateSpELExpression(data, expressionString, beanClass, "data");
    }

    /**
     * 计算目标类的表达式
     * <pre>
     *     {@code
     *     eg1:
     *      Dict data = GXSpELToolUtils.calculateSpELExpression(entity , "test == 'world' ? {'username':'枫叶'} :{'kk' : 'jack'}" , Dict.class);
     *      String data = GXSpELToolUtils.calculateSpELExpression(entity , "test == 'world' ? '枫叶' :{'jack'" , Dict.class);
     *     eg2:
     *      final TestDTO testDTO = new TestDTO();
     *      testDTO.setTest("test");
     *      testDTO.setContent("content");
     *      testDTO.setRoster(Arrays.asList("hello", "jack", "jerry"));
     *      GXSpELToolUtils.calculateSpELExpression(testDTO, "roster[1]", String.class);
     *     }
     * </pre>
     *
     * @param targetObject     目标对象
     * @param expressionString 表达式
     * @param beanClazz        返回对象的类型
     * @return T
     */
    public static <T> T calculateSpELExpression(Object targetObject, String expressionString, Class<T> beanClazz) {
        if (Objects.isNull(targetObject)) {
            return GXCommonUtils.getClassDefaultValue(beanClazz);
        }
        try {
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext(targetObject);
            return parser.parseExpression(expressionString).getValue(context, beanClazz);
        } catch (SpelEvaluationException e) {
            LOG.error("SpEL表达式失败 , 表达式 : {} , 异常信息 : {}", expressionString, e.getMessage());
        }
        return GXCommonUtils.getClassDefaultValue(beanClazz);
    }

    /**
     * 在计算时动态为目标对象设置值
     *
     * <pre> {@code
     *  public class Inventor{
     *      private String name;
     *      private int age;
     *      private int roleId;
     *  }
     *  Inventor targetObj = new Inventor();
     *  String value = assignmentSpELExpression(targetObj , Dict.create().set("name" , "枫叶思源").set("age" , 12) , "name" , String.class);
     *  // 如果目标对象没有对应的字段存在
     *  // 则会抛出异常信息,eg:
     *  // String value = assignmentSpELExpression(targetObj , Dict.create().set("name111" , "枫叶思源").set("age" , 12) , "name" , String.class);
     * }</pre>
     *
     * @param targetObj 目标对象
     * @param data      动态设置的值
     * @param targetKey 目标对象的key
     * @param clazz     返回的值的类型
     * @return T
     */
    public static <T> T assignmentSpELExpression(Object targetObj, Dict data, String targetKey, Class<T> clazz) {
        final StandardEvaluationContext inventorContext = new StandardEvaluationContext(targetObj);
        final ExpressionParser parser = new SpelExpressionParser();
        if (data.isEmpty()) {
            return GXCommonUtils.getClassDefaultValue(clazz);
        }
        data.forEach((key, value) -> parser.parseExpression(key).setValue(inventorContext, value));
        return parser.parseExpression(targetKey).getValue(inventorContext, clazz);
    }

    /**
     * 动态注册函数并调用它进行计算
     *
     * <pre>{@code
     *   public class StringUtils {
     *       public static String concat(String str1, String str2, String str3) {
     *         return "链接字符串 : " + str1 + " ---- " + str2 + " ==== " + str3;
     *     }
     *   }
     *   String retVal = registerFunctionSpELExpression(StringUtils.class, String.class, "concat", new Class[]{String.class, String.class, String.class}, "hello", "britton", "枫叶思源");
     *   }</pre>
     *
     * @param targetClass      提供函数的目标类
     * @param methodName       目标方法
     * @param clazz            返回值的类型
     * @param methodParamTypes 方法参数的类型
     * @param params           调用参数的类型(实参
     * @return T
     */
    public static <T> T registerFunctionSpELExpression(Class<?> targetClass, String methodName, Class<T> clazz, Class<?>[] methodParamTypes, Object... params) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("params", params);
        if (methodNotExists(targetClass, methodName, methodParamTypes)) return null;
        context.registerFunction(methodName, ReflectUtil.getMethod(targetClass, methodName, methodParamTypes));
        final String format = CharSequenceUtil.format("#{}({})", methodName, parsePlaceholderParams(methodParamTypes, params));
        return parser.parseExpression(format).getValue(context, clazz);
    }

    /**
     * 调用指定bean中的方法
     *
     * <pre>
     *     {@code
     *     String s = GXSpELToolUtils.callBeanMethodSpELExpression(
     *     HelloService.class,
     *     "hello",
     *     String.class,
     *     new Class[]{String.class, int.class, TestEntity.class},
     *     "枫叶思源", 98 , testEntity);
     *     }
     * </pre>
     *
     * @param beanClazz        Bean的Class对象
     * @param methodName       方法名字
     * @param clazz            返回的类型
     * @param methodParamTypes 参数的类型
     * @param params           参数列表
     * @return T
     */
    public static <T> T callBeanMethodSpELExpression(Class<?> beanClazz, String methodName, Class<T> clazz, Class<?>[] methodParamTypes, Object... params) {
        final Object beanObj = GXSpringContextUtils.getBean(beanClazz);
        if (Objects.isNull(beanObj)) {
            return null;
        }
        if (methodNotExists(beanClazz, methodName, methodParamTypes)) return null;
        final ExpressionParser expressionParser = new SpelExpressionParser();
        final StandardEvaluationContext context = new StandardEvaluationContext(beanObj);
        final String expressionString = CharSequenceUtil.format("{}({})", methodName, parseArgumentParams(context, methodParamTypes, params));
        return expressionParser.parseExpression(expressionString).getValue(context, clazz);
    }

    /**
     * 判断目标类型是否包含指定的签名方法
     *
     * @param beanClazz        目标类型
     * @param methodName       方法名字
     * @param methodParamTypes 方法参数类型
     * @return 是否存在  true 存在  false 不存在
     */
    private static boolean methodNotExists(Class<?> beanClazz, String methodName, Class<?>[] methodParamTypes) {
        final Method method = ReflectUtil.getMethod(beanClazz, methodName, methodParamTypes);
        if (Objects.isNull(method)) {
            final String paramStr = Arrays.stream(methodParamTypes).map(Class::getSimpleName).collect(Collectors.joining(","));
            LOG.error(METHOD_NOT_FOUND_TIPS_TEMPLATE, beanClazz.getSimpleName(), methodName, paramStr);
            return true;
        }
        return false;
    }

    /**
     * 调用指定对象中的方法
     * <pre>
     *  {@code
     *    String s = GXSpELToolUtils.callTargetObjectMethodSpELExpression(
     *    GXSpringContextUtils.getBean(HelloService.class),
     *    "hello",
     *    String.class,
     *    new Class[]{String.class, int.class,TestEntity.class},
     *    "枫叶思源", 98 , testEntity);
     *  }
     * </pre>
     *
     * @param targetObject     目标对象
     * @param methodName       方法名字
     * @param clazz            返回的类型
     * @param methodParamTypes 参数的类型
     * @param params           参数列表
     * @return T
     */
    public static <T> T callTargetObjectMethodSpELExpression(@NotNull Object targetObject, String methodName, Class<T> clazz, Class<?>[] methodParamTypes, Object... params) {
        final Method method = ReflectUtil.getMethod(targetObject.getClass(), methodName, methodParamTypes);
        if (Objects.isNull(method)) {
            final String paramStr = Arrays.stream(methodParamTypes).map(Class::getSimpleName).collect(Collectors.joining(","));
            LOG.info(METHOD_NOT_FOUND_TIPS_TEMPLATE, targetObject.getClass().getSimpleName(), methodName, paramStr);
            return null;
        }
        final ExpressionParser expressionParser = new SpelExpressionParser();
        final StandardEvaluationContext context = new StandardEvaluationContext(targetObject);
        final String expressionString = CharSequenceUtil.format("{}({})", methodName, parseArgumentParams(context, methodParamTypes, params));
        return expressionParser.parseExpression(expressionString).getValue(context, clazz);
    }

    /**
     * 将匹配的表达式设置新值
     * <pre>
     * {@code
     *  final TestDTO testDTO = new TestDTO();
     *  testDTO.setTest("ceshi");
     *  testDTO.setContent("content");
     *  testDTO.setRoster(Arrays.asList("hello", "jack", "jerry"));
     *  String oldValue = GXSpELToolUtils.setValueBySpELExpression(testDTO, "roster[1]", String.class, "newValue");
     * }
     * </pre>
     *
     * @param targetObject     目标对象
     * @param expressionString 表达式
     * @param oldValueClazz    旧值类型
     * @param newValue         新值
     * @return 旧值
     */
    public static <T> T setValueBySpELExpression(Object targetObject, String expressionString, Class<T> oldValueClazz, T newValue) {
        final ExpressionParser expressionParser = new SpelExpressionParser();
        final StandardEvaluationContext context = new StandardEvaluationContext(targetObject);
        final T oldValue = expressionParser.parseExpression(expressionString).getValue(context, oldValueClazz);
        expressionParser.parseExpression(expressionString).setValue(context, newValue);
        return oldValue;
    }

    /**
     * 将匹配的表达式设置新值
     * <pre>
     *     {@code
     *       final Dict dict = Dict.create().set("username", "枫叶思源");
     *       final String oldValues = GXSpELToolUtils.setValueBySpELExpression(dict, "#data['username']", String.class, "newValues");
     *     }
     * </pre>
     *
     * @param dict             目标对象
     * @param expressionString 表达式
     * @param oldValueClazz    旧值类型
     * @param newValue         新值
     * @return 旧值
     */
    public static <T> T setValueBySpELExpression(Dict dict, String expressionString, Class<T> oldValueClazz, T newValue) {
        ExpressionParser expressionParser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        String dataKey = "data";
        context.setVariable(dataKey, dict);
        final T oldValue = expressionParser.parseExpression(expressionString).getValue(context, oldValueClazz);
        expressionParser.parseExpression(expressionString).setValue(context, newValue);
        return oldValue;
    }

    /**
     * 解析可变参数占位符
     *
     * @param methodParamTypes 参数的类型
     * @param params           具体参数
     * @return StringBuilder
     */
    private static String parsePlaceholderParams(Class<?>[] methodParamTypes, Object... params) {
        StringBuilder methodParam = new StringBuilder();
        for (int i = 0; i < methodParamTypes.length; i++) {
            if (i >= params.length) {
                methodParam.append("null , ");
                continue;
            }
            methodParam.append("#params[").append(i).append("] , ");
        }
        return CharSequenceUtil.subBefore(methodParam.toString(), ',', true);
    }

    /**
     * 解析可变参数实际参数
     *
     * @param methodParamTypes 参数的类型
     * @param params           具体参数
     * @return StringBuilder
     */
    private static String parseArgumentParams(EvaluationContext context, Class<?>[] methodParamTypes, Object... params) {
        StringBuilder methodParam = new StringBuilder();
        for (int i = 0; i < methodParamTypes.length; i++) {
            if (i >= params.length) {
                methodParam.append("null , ");
                continue;
            }
            final Class<?> type = methodParamTypes[i];
            if (ClassUtil.isSimpleValueType(type)) {
                if (type.getSimpleName().equalsIgnoreCase("string")) {
                    methodParam.append("'").append(params[i]).append("' , ");
                } else {
                    methodParam.append(params[i]).append(" , ");
                }
            } else {
                final String name = CharSequenceUtil.replace(type.getName(), ".", "");
                context.setVariable(name, params[i]);
                methodParam.append("#").append(name).append(" , ");
            }
        }
        return CharSequenceUtil.subBefore(methodParam.toString(), ',', true);
    }
}
