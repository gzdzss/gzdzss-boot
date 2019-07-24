package com.gzdzss.redis.autoconfigure;

import com.gzdzss.redis.plugin.lock.DistributedLock;
import com.gzdzss.redis.plugin.lock.RedisDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */

@Slf4j
@SuppressWarnings("all")
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class GzdzssRedisDistributedLockAutoConfiguration {

    public GzdzssRedisDistributedLockAutoConfiguration() {
        log.debug("Enable Gzdzss Redis Distributed Lock Configuration.");
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public DistributedLock redisDistributedLock(RedisTemplate<String, String> redisTemplate) {
        return new RedisDistributedLock(redisTemplate);
    }

}
