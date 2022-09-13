package com.galua.urlshortener.controller;

import com.galua.urlshortener.model.Url;
import com.galua.urlshortener.service.UrlManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlControllerTest {

    @Mock
    private UrlManagerService urlManager;

    @InjectMocks
    private UrlController urlController;

    private final Url url = Url.builder()
            .originalUrl("http://www.google.com")
            .shortenUrl("http://localhost:8080/sdsadfsf")
            .key("sdsadfsf")
            .build();

    @Test
    public void testShortenUrl() {
        // given
        when(urlManager.shortenUrl(anyString())).thenReturn(url);

        // when
        ResponseEntity<String> response = urlController.shortenUrl("http://www.google.com");

        // then
        assertNotNull(response);
        assertEquals(response.getBody(), url.getShortenUrl());

        verify(urlManager).shortenUrl("http://www.google.com");
        verifyNoMoreInteractions(urlManager);
    }

    @Test
    public void testShortenUrlWithInvalidUrl() {
        // given - when
        ResponseEntity<String> response = urlController.shortenUrl("htt:/.google.com");

        // then
        assertNotNull(response);
        assertEquals("Invalid URL", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verifyNoInteractions(urlManager);
    }

    @Test
    public void testGetUrl() {
        // given
        when(urlManager.getUrlByKey(anyString())).thenReturn(url);

        // when
        ResponseEntity<String> response = urlController.getUrl("http://www.google.com");

        // then
        assertNotNull(response);
        assertEquals(response.getBody(), url.getOriginalUrl());

        verify(urlManager).getUrlByKey("http://www.google.com");
        verifyNoMoreInteractions(urlManager);
    }

    @Test
    public void testGetUrlWithInvalidKey() {
        // given
        when(urlManager.getUrlByKey(anyString())).thenReturn(null);

        // when
        ResponseEntity<String> response = urlController.getUrl("12345");

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(urlManager).getUrlByKey("12345");
        verifyNoMoreInteractions(urlManager);
    }
}
