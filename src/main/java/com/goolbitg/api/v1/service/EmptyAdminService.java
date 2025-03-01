package com.goolbitg.api.v1.service;

import com.goolbitg.api.v1.exception.CommonException;

/**
 * EmptyAdminService
 */
public class EmptyAdminService implements AdminService {

    @Override
    public void authenticateAdmin(String password) throws CommonException {
        throw new UnsupportedOperationException("Unimplemented method 'authenticateAdmin'");
    }

}
