package com.perkflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PerkFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerkFlowApplication.class, args);
    }
}
