package com.goolbitg.api.v1.service;

import com.goolbitg.api.v1.exception.AuthException;
import com.goolbitg.api.v1.exception.CommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * AdminServiceImpl
 */
@Service
@Profile({ "local", "dev", "prod" })
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
