package com.goolbitg.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.goolbitg.api.exception.AuthException;
import com.goolbitg.api.exception.CommonException;

/**
 * AdminServiceImpl
 */
@Service
@Profile({ "local", "dev" })
public class AdminServiceImpl implements AdminService {

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void authenticateAdmin(String password) throws CommonException {
        if (!password.equals(adminPassword)) {
            throw AuthException.authenticationFailed();
        }
    }

}
