package com.gzdzss.redis.plugin.lock;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisDistributedLockAspect {

    /**
     * 锁的资源，key。支持spring El表达式
     */
    String key() default "'default'";

    /**
     * 持锁时间,单位毫秒，默认0.5小时
     */
    long keepMills() default 30 * 60 * 1000;

    /**
     * 当获取失败时候动作
     */
    LockFailAction action() default LockFailAction.GIVEUP;

    /**
     * 档获取锁失败时候响应结果
     */
    LockFailResult result() default LockFailResult.THROW_ERROR;

    /**
     * 重试的间隔时间,设置GIVEUP忽略此项
     */
    long sleepMills() default 200;

    /**
     * 重试次数
     */
    int retryTimes() default 5;

    /**
     * 获取锁失败时采取的动作
     */
    enum LockFailAction {
        /**
         * 放弃
         */
        GIVEUP,
        /**
         * 继续
         */
        CONTINUE
    }

    /**
     * 获取失败时的结果
     */
    enum LockFailResult{
        /**
         * 返回 null
         */
        NULL,
        /**
         * 抛出异常
         */
        THROW_ERROR
    }
}
