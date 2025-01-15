package com.goolbitg.api.service;

import com.goolbitg.api.model.UserDto;

/**
 * UserService
 */
public interface UserService {

    UserDto getUser(String id);
}
