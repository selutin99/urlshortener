package com.galua.urlshortener.testcontainers;

import com.galua.urlshortener.model.Url;
import com.galua.urlshortener.service.UrlManagerService;
import com.google.common.hash.Hashing;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.Charset;

import static junit.framework.TestCase.assertEquals;

/**
 * Requires Docker running on the machine to run without errors
 */
@SpringBootTest
public class UrlManagerServiceIntegrationTest {

    static {
        GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine"))
                .withExposedPorts(6379);
        redis.start();
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }

    @Autowired
    private UrlManagerService urlManagerService;

    @Test
    void shortenUrl() {
        // given
        String longUrl = "http://www.google.com";

        // when
        Url url = urlManagerService.shortenUrl(longUrl);

        // then
        assertEquals(longUrl, url.getOriginalUrl());
        assertEquals(Hashing.murmur3_32_fixed().hashString(longUrl, Charset.defaultCharset()).toString(), url.getKey());
    }

    @Test
    void getUrlByKey() {
        // given
        String longUrl = "http://www.google.com";

        // when
        Url url = urlManagerService.shortenUrl(longUrl);
        Url urlByKey = urlManagerService.getUrlByKey(url.getKey());

        // then
        assertEquals(urlByKey.getOriginalUrl(), url.getOriginalUrl());
        assertEquals(urlByKey.getShortenUrl(), url.getShortenUrl());
    }
}
