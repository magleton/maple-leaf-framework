package cn.maple.core.framework.util;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.exception.GXBeanValidateException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;

/**
 * <p>
 * hibernate-validator校验工具类
 * </p>
 *
 * @author zj chen <britton@126.com>
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 */
public class GXValidatorUtils {
    /**
     * 验证器
     */
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private GXValidatorUtils() {

    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws GXBeanValidateException 校验不通过，则报GXBeanValidateException异常
     */
    public static void validateEntity(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            final Dict dict = Dict.create();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                final String rootBeanName = CharSequenceUtil.lowerFirst(constraint.getRootBean().getClass().getSimpleName());
                final String currentFormName =/* rootBeanName + "." +*/ constraint.getPropertyPath().toString();
                dict.set(currentFormName, CharSequenceUtil.format("{} , value = {}", constraint.getMessage(), constraint.getInvalidValue()));
            }
            throw new GXBeanValidateException("数据验证错误", HttpStatus.HTTP_INTERNAL_ERROR, dict);
        }
    }

    /**
     * 校验对象
     *
     * @param object   待校验对象
     * @param jsonName json的名字
     * @param groups   待校验的组
     * @throws GXBeanValidateException 校验不通过，则报GXBeanValidateException异常
     */
    public static void validateEntity(Object object, String jsonName, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            final Dict dict = Dict.create();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                String currentFormName = constraint.getPropertyPath().toString();
                if (CharSequenceUtil.isNotBlank(jsonName)) {
                    currentFormName = jsonName + "." + currentFormName;
                }
                String message = constraint.getMessage();
                //currentFormName = CharSequenceUtil.toSymbolCase(currentFormName, '_');
                if (constraint.getMessageTemplate().contains("{fieldName}")) {
                    final Dict param = Dict.create().set("fieldName", currentFormName);
                    message = StrUtil.format(constraint.getMessageTemplate(), param);
                }
                dict.putIfAbsent(currentFormName, message);
            }
            throw new GXBeanValidateException("数据验证错误", HttpStatus.HTTP_INTERNAL_ERROR, dict);
        }
    }
}
