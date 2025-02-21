package com.goolbitg.api.repository;

import java.util.Optional;

/**
 * UserTokenRepository
 */
public interface UserTokenRepository {
    void save(String userId, String refreshToken);
    Optional<String> findUserIdByRefreshToken(String refreshToken);
    void deleteByUserId(String userId);
    boolean isLoggedIn(String userId);
}
