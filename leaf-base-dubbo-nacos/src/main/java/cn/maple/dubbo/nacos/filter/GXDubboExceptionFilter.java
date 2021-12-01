package cn.maple.dubbo.nacos.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.exception.GXSentinelFlowException;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.filter.ExceptionFilter;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;

@Activate(group = CommonConstants.PROVIDER)
public class GXDubboExceptionFilter extends ExceptionFilter {
    private final Logger logger = LoggerFactory.getLogger(GXDubboExceptionFilter.class);

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            try {
                Throwable exception = appResponse.getException();

                //(如果是checked异常则直接抛异常) directly throw if it's checked exception
                if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
                    return;
                }

                // (如果触发了Sentinel的限制规则,则直接抛异常)deal GXSentinelBlockException RuntimeException
                if (exception.getMessage().equals("SentinelBlockException: FlowException")) {
                    Dict data = Dict.create().set("methodName", invocation.getMethodName())
                            .set("arguments", CollUtil.toList(invocation.getArguments()))
                            .set("interfaceName", invocation.getInvoker().getInterface());
                    exception = new GXSentinelFlowException("服务繁忙,请稍后重试!!", HttpStatus.HTTP_NOT_ACCEPTABLE, data);
                    appResponse.setException(exception);
                    return;
                }

                // 自定义异常处理方式
                if (exception instanceof GXBusinessException) {
                    return;
                }

                // (如果是接口方法声明的异常则直接抛异常)directly throw if the exception appears in the signature
                try {
                    Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                    Class<?>[] exceptionClasses = method.getExceptionTypes();
                    for (Class<?> exceptionClass : exceptionClasses) {
                        if (exception.getClass().equals(exceptionClass)) {
                            return;
                        }
                    }
                } catch (NoSuchMethodException e) {
                    return;
                }

                // (如果在方法声明中没有发现这个异常, 则在日志中以error级别打印这个异常) for the exception not found in method's signature, print ERROR message in server's log.
                logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getServiceContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);

                //(如果这个异常与接口在同一个jar包中 则直接抛异常) directly throw if exception class and interface class are in the same jar file.
                String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
                    return;
                }
                //  (如果是jdk异常则直接抛异常) directly throw if it's JDK exception
                String className = exception.getClass().getName();
                if (className.startsWith("java.") || className.startsWith("javax.")) {
                    return;
                }

                // (如果是dubbo的异常则直接抛异常) directly throw if it's dubbo exception
                if (exception instanceof RpcException) {
                    return;
                }

                // otherwise, wrap with RuntimeException and throw back to the client
                appResponse.setException(exception);
            } catch (Throwable e) {
                logger.warn("Fail to ExceptionFilter when called by " + RpcContext.getServiceContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
            }
        }
    }
}
