package com.goolbitg.api.v1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.goolbitg.api.v1.component.RequestLogginInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RequestLogginInterceptor requestLogginInterceptor;

    @Value("${logger.http.request.enabled}")
    private boolean loggerEnabled;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (loggerEnabled) {
            log.info("Http request logger is enabled");
            registry.addInterceptor(requestLogginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/health");
        }
    }

}
