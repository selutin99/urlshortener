package com.galua.urlshortener.service;

import com.galua.urlshortener.model.Url;
import com.galua.urlshortener.util.EnvUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlManagerServiceTest {

    @Mock
    private EnvUtil envUtil;

    @Mock
    private RedisTemplate<String, Url> redisTemplate;

    @InjectMocks
    private UrlManagerService urlManagerService;

    private final Url url = Url.builder()
            .originalUrl("http://www.google.com")
            .shortenUrl("http://localhost:8080/sdsadfsf")
            .key("sdsadfsf")
            .build();

    @Test
    public void testGetUrlByKey() {
        // given
        ValueOperations<String, Url> valueOperationsMock = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperationsMock);
        when(valueOperationsMock.get(anyString())).thenReturn(url);

        // when
        Url response = urlManagerService.getUrlByKey("sdsadfsf");

        // then
        assertNotNull(response);
        assertEquals(response, url);

        verify(redisTemplate.opsForValue()).get("sdsadfsf");
        verifyNoMoreInteractions(redisTemplate);
    }

    @Test
    public void testShortenUrl() {
        // given
        Url url = Url.builder()
                .originalUrl("http://www.google.com")
                .shortenUrl("http://localhost:8080/sdsadfsf")
                .key("4170157c")
                .build();

        ValueOperations<String, Url> valueOperationsMock = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperationsMock);
        when(valueOperationsMock.get(anyString())).thenReturn(null);
        when(envUtil.getShortenUrl(anyString())).thenReturn("http://localhost:8080/sdsadfsf");

        // when
        Url response = urlManagerService.shortenUrl("http://www.google.com");

        // then
        assertNotNull(response);
        assertEquals(response, url);

        verify(redisTemplate.opsForValue()).get("4170157c");
        verify(envUtil).getShortenUrl("4170157c");
        verifyNoMoreInteractions(redisTemplate);
    }

    @Test
    public void testShortenUrlWithExistingUrl() {
        // given
        ValueOperations<String, Url> valueOperationsMock = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperationsMock);
        when(valueOperationsMock.get(anyString())).thenReturn(url);

        // when
        Url response = urlManagerService.shortenUrl("http://www.google.com");

        // then
        assertNotNull(response);
        assertEquals(response, url);

        verify(redisTemplate.opsForValue()).get("4170157c");
        verifyNoInteractions(envUtil);
        verifyNoMoreInteractions(redisTemplate);
    }
}
