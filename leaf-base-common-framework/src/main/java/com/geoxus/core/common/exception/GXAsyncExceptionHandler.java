package com.geoxus.core.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class GXAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        log.error("Exception message - {} , Method name - {}", throwable.getMessage(), method.getName());
        for (Object param : params) {
            log.error("Parameter value - {}", param);
        }
    }
}
