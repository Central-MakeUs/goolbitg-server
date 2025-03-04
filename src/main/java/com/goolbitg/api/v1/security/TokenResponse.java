package com.goolbitg.api.v1.security;

/**
 * TokenResponse
 */
public record TokenResponse(String accessToken, String refreshToken) {
}
