package com.memariyan.components.scheduling.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedissonLockService implements LockService {

    private final RedissonClient redissonClient;

    @Override
    public boolean acquire(String lockName, int waitTime, int leaseTime) throws Exception {
        return redissonClient.getLock(lockName).tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
    }

    @Override
    public void release(String lockName) throws Exception {
        redissonClient.getLock(lockName).unlock();
    }
}
