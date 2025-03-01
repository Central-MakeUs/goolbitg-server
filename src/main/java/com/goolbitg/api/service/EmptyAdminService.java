package com.goolbitg.api.service;

import com.goolbitg.api.exception.CommonException;

/**
 * EmptyAdminService
 */
public class EmptyAdminService implements AdminService {

    @Override
    public void authenticateAdmin(String password) throws CommonException {
        throw new UnsupportedOperationException("Unimplemented method 'authenticateAdmin'");
    }

}
