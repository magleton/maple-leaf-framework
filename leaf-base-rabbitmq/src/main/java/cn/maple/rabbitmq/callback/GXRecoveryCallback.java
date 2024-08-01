package cn.maple.rabbitmq.callback;

import org.springframework.retry.RetryContext;

public interface GXRecoveryCallback {
    Object recover(RetryContext retryContext) throws Exception;
}
