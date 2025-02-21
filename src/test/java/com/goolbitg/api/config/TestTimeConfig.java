package com.goolbitg.api.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * TestTimeConfig
 */
@Configuration
@Profile("test")
public class TestTimeConfig {

    @Bean
    public Clock clock() {
        return Clock.fixed(Instant.parse("2025-01-23T00:00:00Z"), ZoneId.of("UTC"));
    }
}
