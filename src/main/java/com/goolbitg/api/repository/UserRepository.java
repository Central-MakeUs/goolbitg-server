package com.goolbitg.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.goolbitg.api.entity.User;

/**
 * UserRepository
 */
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByKakaoId(String kakaoId);
    Optional<User> findByAppleId(String appleId);
    Optional<User> findByNickname(String nickname);

}
