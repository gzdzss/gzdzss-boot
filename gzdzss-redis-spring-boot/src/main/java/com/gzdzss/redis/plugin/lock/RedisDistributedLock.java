package com.gzdzss.redis.plugin.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */

@Slf4j
public class RedisDistributedLock extends AbstractDistributedLock {

    private RedisTemplate<String, String> redisTemplate;

    private ThreadLocal<String> lockFlag = new ThreadLocal<>();

    public RedisDistributedLock(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean lock(final String key, final long expire, int retryTimes, final long sleepMillis) {
        boolean result = setRedis(key, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while (!Thread.currentThread().isInterrupted() && (!result) && retryTimes-- > 0) {
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("get lock occured an exception,", e);
                return false;
            }

            result = setRedis(key, expire);
            log.debug("retry get [{}] lock: {} ,remaining times: {}", key, result, retryTimes);
        }
        return result;
    }

    @Override
    public boolean releaseLock(final String key) {
        boolean result = false;
        try {
            if (redisTemplate.opsForValue().get(key).equals(lockFlag.get())) {
                redisTemplate.delete(key);
                lockFlag.remove();
                log.debug("release [{}] lock", key);
            }
            result = true;
        } catch (Exception e) {
            log.error("release lock occured an exception", e);
        }
        return result;
    }


    private boolean setRedis(final String key, final long expire) {
        boolean result = false;
        try {
            lockFlag.set(UUID.randomUUID().toString());
            result = redisTemplate.opsForValue().setIfAbsent(key, lockFlag.get(), expire, TimeUnit.MILLISECONDS);
            if (result) {
                log.debug("lock [{}] expire: {}ms", key, expire);
            }
        } catch (Exception e) {
            log.error("set redis occured an exception", e);
        }
        log.debug("get [{}] lock: {}", key, result);
        return result;
    }
}
