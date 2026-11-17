package com.perkflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
class PerkFlowApplicationTests {

    @Autowired
    private Environment environment;

    @Autowired
    private PerkFlowProperties perkFlowProperties;

    @Value("${perkflow.environment}")
    private String injectedEnvironment;

    @Test
    void contextLoads() {
        assertEquals(expectedEnvironment(), injectedEnvironment);
        assertEquals(injectedEnvironment, perkFlowProperties.environment());
        assertEquals(System.getenv().getOrDefault("PERKFLOW_DISPLAY_NAME", "PerkFlow"),
                perkFlowProperties.displayName());
        assertEquals(expectedPort(), environment.getProperty("server.port"));
        assertEquals(expectedApplicationLogLevel(), environment.getProperty("logging.level.com.perkflow"));
        assertEquals(expectedRootLogLevel(), environment.getProperty("logging.level.root"));
        if ("dev".equals(selectedProfile())) {
            assertEquals("WARN", environment.getProperty("logging.level.org.springframework"));
        }
    }

    private String expectedEnvironment() {
        String override = System.getenv("PERKFLOW_ENVIRONMENT");
        if (override != null) {
            return override;
        }

        return switch (selectedProfile()) {
            case "dev" -> "development";
            case "test" -> "test";
            case "prod" -> "production";
            default -> "default";
        };
    }

    private String expectedPort() {
        String override = System.getenv("SERVER_PORT");
        if (override != null) {
            return override;
        }

        return switch (selectedProfile()) {
            case "dev" -> "8082";
            case "test" -> "8083";
            case "prod" -> "8080";
            default -> "8081";
        };
    }

    private String expectedApplicationLogLevel() {
        return "dev".equals(selectedProfile()) ? "DEBUG" : "INFO";
    }

    private String expectedRootLogLevel() {
        return switch (selectedProfile()) {
            case "test", "prod" -> "WARN";
            default -> "INFO";
        };
    }

    private String selectedProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        return activeProfiles.length == 0 ? "default" : activeProfiles[activeProfiles.length - 1];
    }
}
