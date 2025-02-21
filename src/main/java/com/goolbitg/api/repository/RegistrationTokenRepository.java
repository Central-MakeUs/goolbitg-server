package com.goolbitg.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.entity.RegistrationToken;

/**
 * RegistrationTokenRepository
 */
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, String> {

    List<RegistrationToken> findAllByUserId(String userId);

}
