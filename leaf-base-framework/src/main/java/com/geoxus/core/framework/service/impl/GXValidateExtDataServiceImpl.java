package com.geoxus.core.framework.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.validator.GXValidateExtDataService;
import com.geoxus.core.datasource.annotation.GXDataSourceAnnotation;
import com.geoxus.core.framework.entity.GXCoreAttributesEntity;
import com.geoxus.core.framework.entity.GXCoreModelEntity;
import com.geoxus.core.framework.service.GXCoreAttributeEnumsService;
import com.geoxus.core.framework.service.GXCoreAttributesService;
import com.geoxus.core.framework.service.GXCoreModelAttributesService;
import com.geoxus.core.framework.service.GXCoreModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@GXDataSourceAnnotation("framework")
public class GXValidateExtDataServiceImpl implements GXValidateExtDataService {
    private static final int VERIFY_VALUE = 1;

    private static final String FIELD_NOT_EXISTS = "{}模型中的{}属性不存在";

    private static final String FIELD_NOT_MATCH = "{}模型中的{}属性格式错误,需要满足({})的验证条件";

    private static final String FIELD_VALUE_NOT_EXISTS = "{}模型中的{}属性的值{}不存在";

    private static final String MODEL_SETTING_NOT_EXISTS = "{}模型不存在,请先配置模型";

    @Resource
    private GXCoreModelService coreModelService;

    @Resource
    private GXCoreAttributesService coreAttributesService;

    @Resource
    private GXCoreAttributeEnumsService coreAttributeEnumsService;

    @Resource
    private GXCoreModelAttributesService coreModelAttributeService;

    @Override
    public boolean validateExtData(Object o, String modelIdentification, String tableFiled, boolean isFullMatchAttribute, ConstraintValidatorContext context) throws UnsupportedOperationException {
        final String jsonStr = JSONUtil.toJsonStr(o);
        if (!JSONUtil.isJson(jsonStr)) {
            return false;
        }
        final int coreModelId = coreModelService.getModelIdByModelIdentification(modelIdentification);
        if (coreModelId <= 0) {
            throw new GXException(CharSequenceUtil.format(MODEL_SETTING_NOT_EXISTS, modelIdentification));
        }
        if (isFullMatchAttribute && !CharSequenceUtil.equals(jsonStr, "{}")) {
            final boolean b = coreModelAttributeService.checkCoreModelFieldAttributes(coreModelId, tableFiled, jsonStr);
            if (!b) {
                throw new GXException(CharSequenceUtil.format("{}字段提交的属性与数据库配置的字段属性不匹配!", tableFiled));
            }
        }
        final GXCoreModelEntity coreModelEntity = coreModelService.getCoreModelByModelId(coreModelId, tableFiled);
        final List<Dict> attributesList = coreModelEntity.getCoreAttributes();
        final Dict validateRule = Dict.create();
        for (Dict dict : attributesList) {
            validateRule.set(CharSequenceUtil.toCamelCase(dict.getStr("attribute_name")), dict.getStr("validation_expression"));
        }
        if (JSONUtil.isJsonObj(jsonStr)) {
            final Dict validateDataMap = Convert.convert(Dict.class, JSONUtil.toBean(jsonStr, Dict.class));
            return !dataValidation(modelIdentification, coreModelId, validateRule, validateDataMap, context, -1);
        } else {
            final JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
            int currentIndex = 0;
            for (Object object : jsonArray) {
                final Dict validateDataMap = Convert.convert(Dict.class, object);
                if (dataValidation(modelIdentification, coreModelId, validateRule, validateDataMap, context, currentIndex++)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 数据验证
     *
     * @param modelIdentification         模型标识
     * @param coreModelId                 模型ID
     * @param parentAttributeValidateRule attribute原始的验证规则
     * @param validateDataMap             需要验证的数据
     * @param context                     验证组件上下文对象
     * @return boolean
     */
    private boolean dataValidation(String modelIdentification, int coreModelId, Dict parentAttributeValidateRule, Map<String, Object> validateDataMap, ConstraintValidatorContext context, int currentIndex) {
        final Set<String> keySet = validateDataMap.keySet();
        for (String attributeName : keySet) {
            final boolean b = coreModelService.checkModelHasAttribute(coreModelId, CharSequenceUtil.toUnderlineCase(attributeName));
            final String errorInfo = currentIndex > -1 ? currentIndex + "." + attributeName : attributeName;
            if (!b) {
                context.buildConstraintViolationWithTemplate(CharSequenceUtil.format(FIELD_NOT_EXISTS, modelIdentification, attributeName)).addPropertyNode(errorInfo).addConstraintViolation();
                return true;
            }
            final GXCoreAttributesEntity attribute = coreAttributesService.getAttributeByAttributeName(attributeName);
            Dict modelAttributesData = coreModelAttributeService.getModelAttributeByModelIdAndAttributeId(coreModelId, attribute.getAttributeId());
            String currentRule = modelAttributesData.getStr("validation_expression");
            if (CharSequenceUtil.isBlank(currentRule)) {
                currentRule = Convert.toStr(parentAttributeValidateRule.get(attributeName));
            }
            if (CharSequenceUtil.isBlank(currentRule) && modelAttributesData.getInt("force_validation") == 0) {
                // 不验证当前数据
                return false;
            }
            if (CharSequenceUtil.isBlank(currentRule)) {
                return true;
            }
            final String value = Convert.toStr(validateDataMap.get(attributeName));
            final boolean isMatch = Pattern.matches(currentRule, value);
            if (!isMatch && modelAttributesData.getInt("required") == VERIFY_VALUE) {
                context.buildConstraintViolationWithTemplate(CharSequenceUtil.format(FIELD_NOT_MATCH, modelIdentification, attributeName, currentRule)).addPropertyNode(errorInfo).addConstraintViolation();
                return true;
            }
            if (!coreAttributeEnumsService.isExistsAttributeValue(attribute.getAttributeId(), value, coreModelId)) {
                context.buildConstraintViolationWithTemplate(CharSequenceUtil.format(FIELD_VALUE_NOT_EXISTS, modelIdentification, attributeName, value)).addPropertyNode(attributeName).addConstraintViolation();
                return true;
            }
        }
        return false;
    }
}
