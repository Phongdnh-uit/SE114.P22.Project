package com.se114p12.backend.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShopLocationConfig {

    @Value("${shop.location.lat}")
    private double lat;

    @Value("${shop.location.lng}")
    private double lng;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
