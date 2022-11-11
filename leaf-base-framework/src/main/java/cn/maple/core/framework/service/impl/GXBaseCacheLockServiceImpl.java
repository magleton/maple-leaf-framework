package cn.maple.core.framework.service.impl;

import cn.maple.core.framework.service.GXBaseCacheLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class GXBaseCacheLockServiceImpl implements GXBaseCacheLockService {
    /**
     * 可重入锁
     */
    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

    @Override
    public Lock getLock(String lockName) {
        return REENTRANT_LOCK;
    }
}
