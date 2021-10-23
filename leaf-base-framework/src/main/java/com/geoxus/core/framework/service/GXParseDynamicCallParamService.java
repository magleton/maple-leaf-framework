package com.geoxus.core.framework.service;

public interface GXParseDynamicCallParamService {
    /**
     * 获取动态调用的方法的参数实参
     *
     * @return Object
     */
    Object getDynamicCallMethodParamValue(String jsonStr);
}
