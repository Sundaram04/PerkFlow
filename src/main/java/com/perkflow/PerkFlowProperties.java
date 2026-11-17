package com.perkflow;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "perkflow")
public record PerkFlowProperties(String environment, String displayName) {
}
