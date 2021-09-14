package com.geoxus.commons.services;

import java.util.List;

public interface GXDynamicCallMethodService {
    /**
     * 动态调用指定服务的参数
     *
     * @param serviceClassName 服务类的名字
     * @param methodName       方法的名字
     * @param parameters       方法参数
     * @return Object
     */
    Object call(String serviceClassName, String methodName, Object parameters);
}
