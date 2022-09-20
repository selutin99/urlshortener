package com.galua.urlshortener.service;

import com.galua.urlshortener.model.Url;
import com.galua.urlshortener.util.EnvUtil;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "urlCache")
public class UrlManagerService {

    private static final Long REDIS_SET_TIMEOUT = 36000L;

    private final RedisTemplate<String, Url> redisTemplate;
    private final EnvUtil envUtil;

    @Cacheable(cacheNames = "url", key = "#key", unless = "#result == null")
    public Url getUrlByKey(String key) {
        log.info("Get URL from Redis");
        return redisTemplate.opsForValue().get(key);
    }

    public Url shortenUrl(String url) {
        // generating murmur3 based hash key as short URL
        String key = Hashing.murmur3_32_fixed().hashString(url, Charset.defaultCharset()).toString();
        // check URL already exists in Redis
        Url existUrl = redisTemplate.opsForValue().get(key);
        if (existUrl != null) {
            log.info("URL {} already store in Redis", existUrl.getOriginalUrl());
            return existUrl;
        }
        // store in redis if URL is new
        Url shortUrlEntry = Url.builder()
                .key(key)
                .originalUrl(url)
                .shortenUrl(envUtil.getShortenUrl(key))
                .build();
        redisTemplate.opsForValue().set(key, shortUrlEntry);
        log.info("Successfully add url {} to Redis", url);
        return shortUrlEntry;
    }
}
