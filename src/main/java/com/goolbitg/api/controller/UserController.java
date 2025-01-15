package com.goolbitg.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.UserApi;
import com.goolbitg.api.model.UserDto;
import com.goolbitg.api.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * UserController
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    @Autowired
    private final UserService userService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

    @Override
    public ResponseEntity<UserDto> getMyInfo() throws Exception {
        UserDto dto = userService.getUser("id0001");
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


}
