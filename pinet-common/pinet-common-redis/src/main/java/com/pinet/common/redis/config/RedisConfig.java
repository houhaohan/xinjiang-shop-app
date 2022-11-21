package com.pinet.common.redis.config;

import com.pinet.common.redis.serde.FastJsonRedisSerDe;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJsonRedisSerDe serDe = new FastJsonRedisSerDe(Object.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serDe);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serDe);
        template.afterPropertiesSet();
        return template;
    }

}
