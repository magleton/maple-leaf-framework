package cn.maple.core.framework.service.impl;

import cn.maple.core.framework.service.GXBaseCacheLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;

@Service
@Slf4j
public class GXBaseCacheLockServiceImpl implements GXBaseCacheLockService {
    @Override
    public Lock getLock(String lockName) {
        return GXBaseCacheLockService.super.getLock(lockName);
    }
}
