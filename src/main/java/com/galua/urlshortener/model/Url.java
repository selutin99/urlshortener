package com.galua.urlshortener.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Url {

    private String key;

    private String originalUrl;

    private String shortenUrl;
}