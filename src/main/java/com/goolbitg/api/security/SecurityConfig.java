package com.goolbitg.api.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${app.security.jwt.keystore-location}")
    private String keyStorePath;
    @Value("${app.security.jwt.keystore-password}")
    private String keyStorePassword;
    @Value("${app.security.jwt.key-alias}")
    private String keyAlias;
    @Value("${app.security.jwt.private-key-passphrase}")
    private String privateKeyPassphrase;

    @Bean
    public KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resStream = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(keyStorePath);
            keyStore.load(resStream, keyStorePassword.toCharArray());
            return keyStore;
        } catch (
            IOException |
            CertificateException |
            NoSuchAlgorithmException |
            KeyStoreException e
        ) {
            log.error("Unable to load keystore: {}", keyStorePath, e);
        }
        throw new IllegalArgumentException("Can't load keystore");
    }

    @Bean
    public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
        try {
            Key key = keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());
            if (key instanceof RSAPrivateKey) {
                return (RSAPrivateKey) key;
            }
        } catch (
            UnrecoverableKeyException |
            NoSuchAlgorithmException |
            KeyStoreException e
        ) {
            log.error("Unable to load private key from keystore: {}", keyStorePath, e);
        }
        throw new IllegalArgumentException();
    }

    @Bean
    public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
        try {
            Certificate certificate = keyStore.getCertificate(keyAlias);
            PublicKey publicKey = certificate.getPublicKey();
            if (publicKey instanceof RSAPublicKey) {
                return (RSAPublicKey) publicKey;
            }
        } catch (KeyStoreException e) {
            log.error("Unable to load public key from keystore: {}", keyStorePath, e);
        }
        throw new IllegalArgumentException();
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(httpBasic -> httpBasic.disable())
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .formLogin(formLogin -> formLogin.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/unregister",
                    "/auth/logout"
                ).authenticated()
                .requestMatchers(
                    "/auth/**",
                    "/admin/**",
                    "test",
                    "/token",
                    "/swagger-ui/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2ResourceServer -> 
                oauth2ResourceServer.jwt(jwt -> 
                    jwt.jwtAuthenticationConverter(getJwtAuthenticationConverter())
                )
            );
        return http.build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
        return new JwtAuthenticationConverter();
    }
}
