package com.memariyan.components.scheduling.service;

public interface LockService {

    boolean acquire(String lockName, int waitTime, int leaseTime) throws Exception;

    void release(String lockName) throws Exception;

}
