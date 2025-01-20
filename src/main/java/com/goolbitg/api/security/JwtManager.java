package com.goolbitg.api.security;

import static com.goolbitg.api.security.Constants.EXPIRATION_TIME;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * JwtManager
 */
@Component
public class JwtManager {
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtManager(
        @Lazy RSAPrivateKey privateKey,
        @Lazy RSAPublicKey publicKey
    ) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String create(Jwt principal) {
        return create(principal.getSubject());
    }

    public String create(UserDetails principal) {
        return create(principal.getUsername());
    }

    public String create(String principal) {
        final long now = System.currentTimeMillis();
        return JWT.create()
            .withIssuer("Goolbitg API")
            .withSubject(principal)
            .withIssuedAt(new Date(now))
            .withExpiresAt(new Date(now + EXPIRATION_TIME))
            .sign(Algorithm.RSA256(publicKey, privateKey));
    }

}
