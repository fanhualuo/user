package com.hehe.user.config;

import com.hehe.common.event.CoreEventDispatcher;
import com.hehe.user.util.MyPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author xieqinghe .
 * @date 2017/11/14 下午4:16
 * @email xieqinghe@terminus.io
 */
@Configuration
public class SpringConfig {


    /**
     * 加密类
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new MyPasswordEncoder();
    }

    /**
     * eventBus
     */
    @Bean
    CoreEventDispatcher coreEventDispatcher() {
        return new CoreEventDispatcher();
    }

    @Bean
    public RedisTemplate<String, OAuth2Authentication> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, OAuth2Authentication> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setDefaultSerializer(jsonRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public GenericJackson2JsonRedisSerializer jsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

}
