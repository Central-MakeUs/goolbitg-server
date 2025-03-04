package com.goolbitg.api.v1.config;

import com.goolbitg.api.v1.service.AdminService;
import com.goolbitg.api.v1.service.EmptyAdminService;
import com.goolbitg.api.v1.service.ImageService;
import com.goolbitg.api.v1.service.LocalImageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.services.s3.S3Client;

/**
 * FallbackConfig
 */
@Configuration
public class FallbackConfig {

    @Bean
    @ConditionalOnMissingBean
    public AdminService emptyAdminService() {
        return new EmptyAdminService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ImageService localImageService() {
        return new LocalImageService();
    }

    @Bean
    @ConditionalOnMissingBean
    public S3Client nullS3Client() {
        return null;
    }
}
