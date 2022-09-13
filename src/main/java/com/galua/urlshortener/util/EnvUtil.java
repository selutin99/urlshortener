package com.galua.urlshortener.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
@RequiredArgsConstructor
public class EnvUtil {

    @Value("${server.port:80}")
    private Integer port;

    @Value("${server.is-https:false}")
    private Boolean isHttps;

    public String getShortenUrl(String key) {
        String protocol = isHttps ? "https" : "http";
        return String.format("%s://%s:%s/%s",
                protocol,
                InetAddress.getLoopbackAddress().getHostAddress(),
                port,
                key);
    }
}
