package com.goolbitg.api.v1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * S3Config
 */
@Configuration
@Profile({"dev", "prod"})
public class S3Config {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }
}
