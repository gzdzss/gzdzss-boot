package com.gzdzss.redis.plugin.lock;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */

public interface DistributedLock {

    /**
     * 默认超时毫秒数
     */
    long TIMEOUT_MILLIS = 30000;

    /**
     * 默认重试次数
     */
    int RETRY_TIMES = 3;

    /**
     * 默认休眠毫秒数
     */
    long SLEEP_MILLIS = 1000;

    /**
     * 获取指定key的当前redis锁
     *
     * @param key key
     * @return 是否获取成功
     */
    boolean lock(String key);

    /**
     * 获取指定key的当前redis锁
     *
     * @param key        key
     * @param retryTimes retryTimes
     * @return 是否获取成功
     */
    boolean lock(String key, int retryTimes);

    /**
     * 获取指定key的当前redis锁
     *
     * @param key         key
     * @param retryTimes  retryTimes
     * @param sleepMillis sleepMillis
     * @return 是否获取成功
     */
    boolean lock(String key, int retryTimes, long sleepMillis);

    /**
     * 获取指定key的当前redis锁
     *
     * @param key    key
     * @param expire expire
     * @return 是否获取成功
     */
    boolean lock(String key, long expire);

    /**
     * 获取指定key的当前redis锁
     *
     * @param key        key
     * @param expire     expire
     * @param retryTimes retryTimes
     * @return 是否获取成功
     */
    boolean lock(String key, long expire, int retryTimes);

    /**
     * 获取指定key的当前redis锁
     *
     * @param key         key 锁的资源，key。
     * @param expire      expire 持锁时间,单位毫秒
     * @param retryTimes  retryTimes 重试次数
     * @param sleepMillis sleepMillis 重试的间隔时间,单位毫秒
     * @return 是否获取成功
     */
    boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    /**
     * 释放指定key的当前redis锁
     *
     * @param key key
     * @return 是否获取成功
     */
    boolean releaseLock(String key);
}
