package com.goolbitg.api.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

/**
 * FirebaseConfig
 */
@Configuration
@ConditionalOnProperty(name = "firebase.auth.enabled")
public class FirebaseConfig {

    @Bean
    public ApplicationRunner firebaseInitializer() {
        return args -> {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();

            FirebaseApp.initializeApp(options);
        };
    }
}
