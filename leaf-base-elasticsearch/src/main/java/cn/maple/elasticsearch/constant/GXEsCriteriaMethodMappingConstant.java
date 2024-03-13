package cn.maple.elasticsearch.constant;

import java.util.HashMap;
import java.util.Map;

public class GXEsCriteriaMethodMappingConstant {
    public static final Map<String, String> METHOD_MAPPING = new HashMap<>();

    static {
        METHOD_MAPPING.put("=", "is");
        METHOD_MAPPING.put("in", "in");
        METHOD_MAPPING.put(">", "greaterThan");
        METHOD_MAPPING.put("<", "lessThan");
        METHOD_MAPPING.put(">=", "greaterThanEqual");
        METHOD_MAPPING.put("<=", "lessThanEqual");
        METHOD_MAPPING.put("!=", "");
        METHOD_MAPPING.put("not in", "notIn");
        METHOD_MAPPING.put("like", "fuzzy");
        METHOD_MAPPING.put("between", "between");
    }

    private GXEsCriteriaMethodMappingConstant() {

    }
}
