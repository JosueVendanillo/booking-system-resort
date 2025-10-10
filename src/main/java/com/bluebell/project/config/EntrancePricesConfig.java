package com.bluebell.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EntrancePricesConfig {

    @Value("${entrance.price.adult}")
    private double adultPrice;

    @Value("${entrance.price.kid}")
    private double kidsPrice;

    public double getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(double adultPrice) {
        this.adultPrice = adultPrice;
    }

    public double getKidsPrice() {
        return kidsPrice;
    }

    public void setKidsPrice(double kidsPrice) {
        this.kidsPrice = kidsPrice;
    }
}
