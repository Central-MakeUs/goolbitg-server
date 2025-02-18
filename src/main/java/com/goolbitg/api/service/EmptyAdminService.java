package com.goolbitg.api.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.goolbitg.api.exception.CommonException;

/**
 * EmptyAdminService
 */
@Service
@Profile({ "test" })
public class EmptyAdminService implements AdminService {

    @Override
    public void authenticateAdmin(String password) throws CommonException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticateAdmin'");
    }

    
}
