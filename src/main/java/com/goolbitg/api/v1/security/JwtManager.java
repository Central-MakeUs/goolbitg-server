package com.goolbitg.api.v1.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
        return create(User.withUsername(principal.getSubject()).build());
    }

    public String create(UserDetails principal) {
        final long now = System.currentTimeMillis();
        return JWT.create()
            .withIssuer("Goolbitg API")
            .withSubject(principal.getUsername())
            .withIssuedAt(new Date(now))
            .withExpiresAt(new Date(now + Constants.EXPIRATION_TIME))
            .withClaim("scp", principal.getAuthorities()
                        .stream()
                        .map(auth -> auth.getAuthority())
                        .toList())
            .sign(Algorithm.RSA256(publicKey, privateKey));
    }

    public String create(String principal) {
        return create(User.withUsername(principal).build());
    }

    public String create(String principal, List<GrantedAuthority> authorities) {
        return create(User.withUsername(principal)
            .authorities(authorities)
            .build());
    }

    public String createPermanent(String principal) {
        final long now = System.currentTimeMillis();
        return JWT.create()
            .withIssuer("Goolbitg API")
            .withSubject(principal)
            .withIssuedAt(new Date(now))
            .withExpiresAt(new Date(now + 999_999_999_999L))
            .sign(Algorithm.RSA256(publicKey, privateKey));
    }
}
