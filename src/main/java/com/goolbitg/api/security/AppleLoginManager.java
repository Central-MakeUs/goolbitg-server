package com.goolbitg.api.security;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.goolbitg.api.service.TimeService;

import lombok.RequiredArgsConstructor;

/**
 * AppleLoginManager
 */
@Component
@RequiredArgsConstructor
public class AppleLoginManager {

    @Value("${app.security.apple.apple-key-id}")
    private String appleKeyId;
    @Value("${app.security.apple.bundle-id}")
    private String bundleId;
    @Value("${app.security.apple.team-id}")
    private String teamId;
    @Value("${app.security.apple.p8-path}")
    private String p8Path;

    private Resource p8Resource;

    private final String APPLE_URL_ROOT = "https://appleid.apple.com";
    private final String APPLE_URL_TOKEN = APPLE_URL_ROOT + "/auth/token";
    private final String APPLE_URL_REVOKE = APPLE_URL_ROOT + "/auth/revoke";

    @Autowired
    private final TimeService timeService;
    @Autowired
    private final ResourceLoader resourceLoader;

    private final RestTemplate restTemplate = new RestTemplate();

    public void unregisterUser(String authCode) throws Exception {
        TokenResponse tokens = createToken(authCode);
        revokeRefreshToken(tokens.refreshToken());
    }


    private String createClientSecret() throws Exception {
        Instant now = timeService.getNowInstant();

        ECPrivateKey privateKey = loadPrivateKey();
        return JWT.create()
                .withHeader(Map.of("alg", "ES256"))
                .withHeader(Map.of("kid", appleKeyId))
                .withIssuer(teamId)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(60 * 60)))
                .withAudience(APPLE_URL_ROOT)
                .withSubject(bundleId)
                .sign(Algorithm.ECDSA256(privateKey));
    }

    private TokenResponse createToken(String authCode) throws Exception {
        // Prepare request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", bundleId);
        requestBody.add("client_secret", createClientSecret());
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", authCode);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create HTTP request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        // Send POST request
        ResponseEntity<Map> response = restTemplate.exchange(APPLE_URL_TOKEN, HttpMethod.POST, request, Map.class);

        Map body = response.getBody();
        return new TokenResponse(
            (String)body.get("access_token"), 
            (String)body.get("refresh_token"));
    }

    private void revokeRefreshToken(String refreshToken) throws Exception {
        // Prepare request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", bundleId);
        requestBody.add("client_secret", createClientSecret());
        requestBody.add("token_type_hint", "refresh_token");
        requestBody.add("token", refreshToken);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create HTTP request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        // Send POST request
        restTemplate.postForEntity(APPLE_URL_REVOKE, request, null);
    }

    private ECPrivateKey loadPrivateKey() throws Exception {
        if (p8Resource == null) {
            p8Resource = resourceLoader.getResource("classpath:" + p8Path);
        }
        String keyContent = new String(Files.readAllBytes(Paths.get(p8Resource.getURI())));

        // Remove PEM headers/footers
        keyContent = keyContent.replace("-----BEGIN PRIVATE KEY-----", "")
                               .replace("-----END PRIVATE KEY-----", "")
                               .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC"); // Elliptic Curve
        return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
    }

}
