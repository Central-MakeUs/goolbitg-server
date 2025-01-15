package com.goolbitg.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goolbitg.api.entity.User;
import com.goolbitg.api.exception.UserException;
import com.goolbitg.api.model.UserDto;
import com.goolbitg.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * UserService
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDto getUser(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> UserException.userNotExist(id));
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setNickname(user.getNickname());
        dto.setBirthday(user.getBirthday());

        return dto;
    }

}
