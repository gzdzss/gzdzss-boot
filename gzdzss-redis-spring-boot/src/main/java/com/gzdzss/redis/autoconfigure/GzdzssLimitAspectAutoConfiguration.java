package com.gzdzss.redis.autoconfigure;

import com.gzdzss.redis.exception.RedisLockException;
import com.gzdzss.redis.plugin.limit.RequestLimitAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */


@Slf4j
@Aspect
@Configuration
@SuppressWarnings("all")
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass(JoinPoint.class)
public class GzdzssLimitAspectAutoConfiguration {

    private static final String DEFAULT_CACHE_PREFIX = "com.gzdzss";

    private RedisTemplate<String, String> redisTemplate;

    public GzdzssLimitAspectAutoConfiguration(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        log.info("Enable Gzdzss Redis Limit Configuration.");
    }

    @Before("(@annotation(com.gzdzss.redis.plugin.limit.RequestLimitAspect))")
    public void limitRequestTimes(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RequestLimitAspect requestLimitAspect = method.getAnnotation(RequestLimitAspect.class);
        String key = String.format("%s:redisLimit:%s.%s:%s", DEFAULT_CACHE_PREFIX, method.getDeclaringClass().getName(), method.getName(), method, joinPoint.getArgs());
        long count = redisTemplate.opsForValue().increment(key, 1);
        //刚创建
        if (count == 1) {
            //设置指定时间后过期
            redisTemplate.expire(key, requestLimitAspect.expire(), TimeUnit.SECONDS);
        }
        log.debug("limit [{}], cur: {}, max:{}", key, count, requestLimitAspect.times());
        if (count > requestLimitAspect.times()) {
            throw new RedisLockException();
        }
    }

}
