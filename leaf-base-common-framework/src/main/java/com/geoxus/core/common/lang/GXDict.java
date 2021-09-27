package com.geoxus.core.common.lang;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unused")
public class GXDict extends Dict {
    /**
     * @param attr 字段名
     * @return JSON字符串
     */
    public String getJSONStr(String attr) {
        Object obj = getObj(attr);
        if (null == obj) {
            return "{}";
        }
        return JSONUtil.toJsonStr(obj);
    }

    /**
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public Integer getInt(String attr, Integer defaultValue) {
        Integer value = getInt(attr);
        if (null == value) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public Long getLong(String attr, Long defaultValue) {
        Long value = getLong(attr);
        if (null == value) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public Double getLong(String attr, Double defaultValue) {
        Double value = getDouble(attr);
        if (null == value) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public Float getLong(String attr, Float defaultValue) {
        Float value = getFloat(attr);
        if (null == value) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public BigDecimal getLong(String attr, BigDecimal defaultValue) {
        BigDecimal value = getBigDecimal(attr);
        if (null == value) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public BigInteger getBigInteger(String attr, BigInteger defaultValue) {
        BigInteger value = getBigInteger(attr);
        if (null == value) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public Boolean getBool(String attr, Boolean defaultValue) {
        Boolean value = getBool(attr);
        if (null == value) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public Short getShort(String attr, Short defaultValue) {
        Short value = getShort(attr);
        if (null == value) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public String getStr(String attr, String defaultValue) {
        String value = getStr(attr);
        if (null == value) {
            value = defaultValue;
        }
        return value;
    }
}
