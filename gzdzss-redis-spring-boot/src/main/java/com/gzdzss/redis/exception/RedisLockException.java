package com.gzdzss.redis.exception;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */

public class RedisLockException extends RuntimeException {

    public RedisLockException() {
        super("系统处理中，请稍后再试。");
    }
}
