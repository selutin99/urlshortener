package com.galua.urlshortener.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galua.urlshortener.model.Url;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableCaching
@RequiredArgsConstructor
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfiguration {

    private final RedisConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, Url> redisTemplate() {
        final RedisTemplate<String, Url> redisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer valueSerializer = new Jackson2JsonRedisSerializer(Url.class);
        valueSerializer.setObjectMapper(objectMapper);
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(valueSerializer);
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration redisCacheConfiguration = config
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new Jackson2JsonRedisSerializer(Url.class)));
        return RedisCacheManager.builder(factory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}
