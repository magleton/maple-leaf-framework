package cn.maple.core.framework.service;

public interface GXDynamicCallMethodService {
    /**
     * 动态调用指定服务的参数
     *
     * @param serviceClassName 服务类的名字
     * @param methodName       方法的名字
     * @param parameters       方法参数
     * @return Object
     */
    Object call(String serviceClassName, String methodName, Object... parameters);

    /**
     * 动态调用指定服务的参数
     *
     * @param target     目标对象
     * @param methodName 方法的名字
     * @param parameters 方法参数
     * @return Object
     */
    Object call(Object target, String methodName, Object... parameters);
}
