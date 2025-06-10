package com.goolbitg.api.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.goolbitg.api.TestTimeService;
import com.goolbitg.api.v1.service.TimeService;

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

    @Bean
    @Primary
    public TimeService timeService() {
        return new TestTimeService();
    }

}
