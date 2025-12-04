package com.bluebell.project.service;

import com.bluebell.project.config.EntrancePricesConfig;
import org.springframework.stereotype.Service;

@Service
public class FeeService {

    private final EntrancePricesConfig entrancePricesConfig;

    public FeeService(EntrancePricesConfig entrancePricesConfig) {
        this.entrancePricesConfig = entrancePricesConfig;
    }


    public EntrancePricesConfig getEntrancePricesConfig() {
        return entrancePricesConfig;
    }
    public void printFees(){
        System.out.println("ENTRANCE FEE ==========");
        System.out.println("ADULT: " +  getEntrancePricesConfig().getAdultPrice());
        System.out.println("KIDS: " +  getEntrancePricesConfig().getKidsPrice());

    }

}
