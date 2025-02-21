package com.goolbitg.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.services.s3.S3Client;

/**
 * NullS3Config
 */
@Configuration
@Profile("!dev")
public class NullS3Config {

    @Bean
    public S3Client nullS3Client() {
        return null;
    }

}
