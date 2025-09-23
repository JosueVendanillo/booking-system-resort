package com.bluebell.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "booking")
public class BookingConfig {

    private Map<String, Integer> prices;

    public Map<String, Integer> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Integer> prices) {
        this.prices = prices;
    }
}
