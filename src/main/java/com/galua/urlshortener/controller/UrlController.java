package com.galua.urlshortener.controller;

import com.galua.urlshortener.model.Url;
import com.galua.urlshortener.service.UrlManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "URL shortener API")
@RequestMapping(value = "/urlshortener")
public class UrlController {

    private final UrlManagerService urlManager;
    private final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    @GetMapping
    @ApiOperation(value = "Create short URL")
    public ResponseEntity<String> shortenUrl(@RequestParam @NotBlank String url) {
        if (!urlValidator.isValid(url)) {
            return ResponseEntity.badRequest().body("Invalid URL");
        }
        Url shortUrlEntry = urlManager.shortenUrl(url);
        return ResponseEntity.ok(shortUrlEntry.getShortenUrl());
    }

    @GetMapping(value = "/{key}")
    @ApiOperation(value = "Get full URL by short link")
    public ResponseEntity<String> getUrl(@PathVariable @NotBlank String key) {
        Url url = urlManager.getUrlByKey(key);
        return url == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(url.getOriginalUrl());
    }
}