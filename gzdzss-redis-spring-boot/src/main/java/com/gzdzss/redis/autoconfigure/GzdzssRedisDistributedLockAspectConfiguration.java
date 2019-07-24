package com.gzdzss.redis.autoconfigure;

import com.gzdzss.redis.exception.RedisLockException;
import com.gzdzss.redis.plugin.lock.DistributedLock;
import com.gzdzss.redis.plugin.lock.RedisDistributedLockAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */

@Slf4j
@Aspect
@Configuration
@SuppressWarnings("all")
@AutoConfigureAfter(GzdzssRedisDistributedLockAutoConfiguration.class)
@ConditionalOnClass(ProceedingJoinPoint.class)
public class GzdzssRedisDistributedLockAspectConfiguration {


    public GzdzssRedisDistributedLockAspectConfiguration() {
        log.debug("Annotated AOP parsing configuration with Redis distributed lock enabled.");
    }


    private static final String DEFAULT_CACHE_PREFIX = "com.gzdzss";

    @Autowired
    private DistributedLock distributedLock;

    private ExpressionParser parser = new SpelExpressionParser();
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();


    @Around("@annotation(com.gzdzss.redis.plugin.lock.RedisDistributedLockAspect)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        //获取方法注解
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RedisDistributedLockAspect lockAction = method.getAnnotation(RedisDistributedLockAspect.class);
        //获取方法属性
        String key = String.format("%s:redisdistributedlock:%s.%s:%s", DEFAULT_CACHE_PREFIX, method.getDeclaringClass().getName(), method.getName(), parse(lockAction.key(), method, pjp.getArgs()));
        try {
            //设置重试次数
            int retryTimes = lockAction.action().equals(RedisDistributedLockAspect.LockFailAction.CONTINUE) ? lockAction.retryTimes() : 0;
            boolean lock = distributedLock.lock(key, lockAction.keepMills(), retryTimes, lockAction.sleepMills());
            if (!lock) {
                log.info("get lock [{}] fail", key);
                if (lockAction.result() == RedisDistributedLockAspect.LockFailResult.NULL) {
                    return null;
                }
                throw new RedisLockException();
            }
            //得到锁,执行方法，释放锁
            return pjp.proceed();
        } finally {
            distributedLock.releaseLock(key);
        }
    }


    /**
     * @param key    表达式
     * @param method 方法
     * @param args   方法参数
     * @return
     * @description 解析spring EL表达式
     */
    private String parse(String key, Method method, Object[] args) {
        if (StringUtils.isEmpty(key) || ObjectUtils.isEmpty(method) || ObjectUtils.isEmpty(args)) {
            return "default";
        }
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                context.setVariable(params[i], args[i]);
            }
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }


}
