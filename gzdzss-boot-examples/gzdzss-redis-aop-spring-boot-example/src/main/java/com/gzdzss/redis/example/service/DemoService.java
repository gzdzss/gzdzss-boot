package com.gzdzss.redis.example.service;

import com.gzdzss.redis.plugin.lock.RedisDistributedLockAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */

@Slf4j
@Service
public class DemoService {


    private static final String LOCK_FLAG = "lock";


    public String hello() {
        return "hello";
    }


    /**
     * 根据方法名锁
     * @return
     */
    @RedisDistributedLockAspect
    public String lock() {
        return "lock";
    }

    /**
     *
     * 获取锁失败 默认返回异常
     * 根据id
     * @param id
     * @return
     * @throws InterruptedException
     */
    @RedisDistributedLockAspect(key= "#id")
    public String lockById(String id) throws InterruptedException {
        if (LOCK_FLAG.equals(id)) {
             log.info("locking");
            Thread.sleep(15000L);
        }
        return "lock";
    }


    /**
     * 获锁失败，返回null
     * @param name
     * @return
     * @throws InterruptedException
     */
    @RedisDistributedLockAspect(key= "#name", result = RedisDistributedLockAspect.LockFailResult.NULL)
    public String lockByName(String name) throws InterruptedException {
        if (LOCK_FLAG.equals(name)) {
            log.info("locking");
            Thread.sleep(15000L);
        }
        return "lock";
    }


    /**
     * 重试五次
     * @param id
     * @return
     * @throws InterruptedException
     */
    @RedisDistributedLockAspect(key = "#id",result = RedisDistributedLockAspect.LockFailResult.NULL,
            action = RedisDistributedLockAspect.LockFailAction.CONTINUE)
    public String continueLock(String id) throws InterruptedException {
        if (LOCK_FLAG.equals(id)) {
            log.info("locking");
            Thread.sleep(15000L);
        }
        return "lock";
    }



}
