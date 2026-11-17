package com.perkflow;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PerkFlowApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerkFlowApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PerkFlowApplication.class, args);
        String environment = context.getEnvironment().getProperty("perkflow.environment", "default");
        LOGGER.info("PerkFlow started in {} environment", environment);
        LOGGER.debug("Active Spring profiles: {}", Arrays.toString(context.getEnvironment().getActiveProfiles()));
    }
}
