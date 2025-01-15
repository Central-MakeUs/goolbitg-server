package com.goolbitg.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.goolbitg.api.entity.User;

/**
 * UserRepository
 */
public interface UserRepository extends CrudRepository<User, String> {

}
