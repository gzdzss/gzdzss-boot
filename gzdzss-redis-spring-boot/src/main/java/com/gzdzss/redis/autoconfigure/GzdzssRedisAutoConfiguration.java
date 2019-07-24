package com.gzdzss.redis.autoconfigure;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */
@SuppressWarnings("all")
@Slf4j
@Configuration
@ConditionalOnClass(GenericFastJsonRedisSerializer.class)
public class GzdzssRedisAutoConfiguration {


    public GzdzssRedisAutoConfiguration() {
        log.info("Enable Gzdzss Redis Configuration.");
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //RedisTemplate操作接口
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置序列化接口
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        //设置默认的Serialize，包含 keySerializer & valueSerializer
        redisTemplate.setDefaultSerializer(fastJsonRedisSerializer);
        //单独设置keySerializer
        redisTemplate.setKeySerializer(fastJsonRedisSerializer);
        //单独设置valueSerializer
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        //单独设置hashKeySerializer
        redisTemplate.setHashKeySerializer(fastJsonRedisSerializer);
        return redisTemplate;
    }
}
