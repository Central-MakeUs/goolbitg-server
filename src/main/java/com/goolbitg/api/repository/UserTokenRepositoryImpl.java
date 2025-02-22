package com.goolbitg.api.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

/**
 * UserTokenRepositoryImpl
 */
@Repository
public class UserTokenRepositoryImpl implements UserTokenRepository {

    private final Map<String, String> store = new HashMap<>();

    @Override
    public void save(String userId, String refreshToken) {
        store.put(userId, refreshToken);
    }

    @Override
    public Optional<String> findUserIdByRefreshToken(String refreshToken) {
        for (Map.Entry<String, String> entry : store.entrySet()) {
            if (entry.getValue().equals(refreshToken)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    @Override
    public void deleteByUserId(String userId) {
        // TODO: 없는 토큰 제거하려고 하면 에러 뜨는가?
        store.remove(userId);
    }

    @Override
    public boolean isLoggedIn(String userId) {
        return store.containsKey(userId);
    }

}
