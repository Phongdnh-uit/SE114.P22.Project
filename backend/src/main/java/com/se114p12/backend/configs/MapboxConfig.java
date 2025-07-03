package com.se114p12.backend.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mapbox")
public class MapboxConfig {
    @Value("${mapbox.access-token}")
    private String accessToken;

    @Value("${mapbox.base-url}")
    private String baseUrl;
}