package com.goolbitg.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.entity.AdminUser;

/**
 * AdminUserRepository
 */
public interface AdminUserRepository extends JpaRepository<AdminUser, String> {

    Optional<AdminUser> findByEmail(String email);

}
